package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* 以CrawlerGroupReader來代表不同query
*   1. snapshot and IDs of active device actors to query
*   2. ID of query request
*   3. reference of the actor who sent the query
*   4. timeout (Behaviors.withTimers)
*
*
* Q1. actor可能停止，且不會把結果回傳給Reader
* A1. - 當拿到query請求時，groupActor會snapshot現存的所有actors，而且只會向當前這些actors要求資料
*     - 如果在snapshot裡的actor沒有回應，就可以回報
*
* Q2. 新actor啟動時，Reader不知情
* A2. 會直接忽略在query請求後才啟動的actor
*
* Q3. actor處理太久
* A3. 增加timeout機制

* */
public class CrawlerGroupReader extends AbstractBehavior<CrawlerGroupReader.Command> {
    private final String requestId;
    private final ActorRef<CrawlerManagerExample.ParseAllResponse> requester;
    private Map<String, CrawlerManagerExample.CrawlerState> successfulReplies = new HashMap<>();
    private final Set<String> waitingCrawlerIds;

    public interface Command{}

    // convert Response of CrawlerActor into WrappedResponse of CrawlerGroupReader
    static class WrappedResponse implements Command {
        final CrawlerActorExample.Response response;
        WrappedResponse(CrawlerActorExample.Response response) {
            this.response = response;
        }
    }

    private static enum QueryTimeout implements Command {
        INSTANCE
    }

    private static class CrawlerTerminated implements Command {
        final String crawlerId;
        private CrawlerTerminated(String crawlerId) {
            this.crawlerId = crawlerId;
        }
    }

    // create CrawlerGroupReader by static factory method from client
    public static Behavior<Command> create(
            Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToActor,
            String requestId,
            ActorRef<CrawlerManagerExample.ParseAllResponse> requester,
            Duration timeout) {
        return Behaviors.setup(context -> Behaviors.withTimers(
                timers -> new CrawlerGroupReader(crawlerIdToActor, requestId, requester, timeout, context, timers)));
    }

    private CrawlerGroupReader(Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToActor, String requestId,
                               ActorRef<CrawlerManagerExample.ParseAllResponse> requester, Duration timeout, ActorContext<Command> context,
                               TimerScheduler<Command> timers) {
        super(context);
        this.requestId = requestId;
        this.requester = requester;

        timers.startSingleTimer(QueryTimeout.INSTANCE, QueryTimeout.INSTANCE, timeout);

        ActorRef<CrawlerActorExample.Response> responseAdapter = context.messageAdapter(CrawlerActorExample.Response.class, WrappedResponse::new);

        for (Map.Entry<String, ActorRef<CrawlerActorExample.Command>> entry : crawlerIdToActor.entrySet()) {
            context.watchWith(entry.getValue(), new CrawlerTerminated(entry.getKey()));
            entry.getValue().tell(new CrawlerActorExample.Read("0L", responseAdapter));
        }
        waitingCrawlerIds = new HashSet<>(crawlerIdToActor.keySet());
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(WrappedResponse.class, this::onResponse)
                .onMessage(CrawlerTerminated.class, this::onCrawlerTerminated)
                .onMessage(QueryTimeout.class, this::onQueryTimeout)
                .build();
    }

    //
    private Behavior<Command> onResponse(WrappedResponse r) {
        CrawlerManagerExample.CrawlerState responseTarget = r.response.result
                        .map(v -> (CrawlerManagerExample.CrawlerState) new CrawlerManagerExample.AvailableCrawler(v))
                        .orElse(CrawlerManagerExample.TargetNotAvailable.INSTANCE);

        String crawlerId = r.response.crawlerId;
        successfulReplies.put(crawlerId, responseTarget);
        waitingCrawlerIds.remove(crawlerId);

        return respondWhenAllQueried();
    }

    private Behavior<Command> onCrawlerTerminated(CrawlerTerminated terminated) {
        if (waitingCrawlerIds.contains(terminated.crawlerId)) {
            successfulReplies.put(terminated.crawlerId, CrawlerManagerExample.FailedCrawler.INSTANCE);
            waitingCrawlerIds.remove(terminated.crawlerId);
        }
        return respondWhenAllQueried();
    }

    private Behavior<Command> onQueryTimeout(QueryTimeout timeout) {
        for (String crawlerId : waitingCrawlerIds) {
            successfulReplies.put(crawlerId, CrawlerManagerExample.Timeout.INSTANCE);
        }
        waitingCrawlerIds.clear();
        return respondWhenAllQueried();
    }

    private Behavior<Command> respondWhenAllQueried() {
        if (waitingCrawlerIds.isEmpty()) {
            requester.tell(new CrawlerManagerExample.ParseAllResponse(requestId, successfulReplies));
            return Behaviors.stopped();
        } else {
            return this;
        }
    }

}
