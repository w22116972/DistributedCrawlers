package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * -> handle registration request for
 *   -> existing crawler actor
 *   -> creating new actor
 * -> onCheckCrawler(): check which crawler actor exists in group
 * -> Death Watch every newly created crawler actors
 * -> remove crawler actors when they are stopped (Death Watch will notify who are stopped)
 *
 * * */

public class CrawlerGroupExample extends AbstractBehavior<CrawlerGroupExample.Command> {
    public static Behavior<Command> create(String crawlerId) {
        return Behaviors.setup(context -> new CrawlerGroupExample(context, crawlerId));
    }

    private final String groupId;
    private final Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToActor = new HashMap<>();

    private CrawlerGroupExample(ActorContext<Command> context, String groupId) {
        super(context);
        this.groupId = groupId;
        context.getLog().info("CrawlerGroup {} started", groupId);
    }

    public interface Command {}

    private class TerminateCrawler implements Command {
        public final ActorRef<CrawlerActorExample.Command> crawler;
        public final String groupId;
        public final String crawlerId;

        TerminateCrawler(ActorRef<CrawlerActorExample.Command> crawler, String groupId, String crawlerId) {
            this.crawler = crawler;
            this.groupId = groupId;
            this.crawlerId = crawlerId;
        }
    }



    private CrawlerGroupExample onCheckCrawler(CrawlerManagerExample.CheckCrawlerRequest checkMessage)  {

        if (this.groupId.equals(checkMessage.groupId)) {
            ActorRef<CrawlerActorExample.Command> crawlerActor = crawlerIdToActor.get(checkMessage.crawlerId);
            if (crawlerActor != null) {
                checkMessage.replyTo.tell(new CrawlerManagerExample.CheckCrawlerResponse(crawlerActor));
            } else {
                getContext().getLog().info("Creating crawler actor for {}", checkMessage.crawlerId);
                crawlerActor = getContext().spawn(CrawlerActorExample.create(groupId, checkMessage.crawlerId), "crawler-" + checkMessage.crawlerId);
                // setup Death Watch on newly created actors
                getContext().watchWith(crawlerActor, new TerminateCrawler(crawlerActor, groupId, checkMessage.crawlerId));

                crawlerIdToActor.put(checkMessage.crawlerId, crawlerActor);
                checkMessage.replyTo.tell(new CrawlerManagerExample.CheckCrawlerResponse(crawlerActor));
            }
        } else {
            getContext().getLog().warn("Ignoring CrawlerActor request for {}. This actor is responsible for {}.", groupId, this.groupId);
        }
        return this;
    }

    private CrawlerGroupExample onTerminated(TerminateCrawler request) {
        getContext().getLog().info("Crawler actor for {} has been terminated", request.crawlerId);
        crawlerIdToActor.remove(request.crawlerId);
        return this;
    }

    private CrawlerGroupExample onCrawlerList(CrawlerManagerExample.ListCrawlersRequest r) {
        r.replyTo.tell(new CrawlerManagerExample.ListCrawlersResponse(r.requestId, crawlerIdToActor.keySet()));
        return this;
    }

    private CrawlerGroupExample onParseAll(CrawlerManagerExample.ParseAllRequest command) {
        // since Java collection is mutable, so here we use defensive copy on mutable Map
        Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToCrawlerActorCopy = new HashMap<>(this.crawlerIdToActor);
        getContext().spawnAnonymous(CrawlerGroupReader.create(crawlerIdToCrawlerActorCopy, command.requestId, command.replyTo, Duration.ofSeconds(3)));

        return this;
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                // check crawler
                .onMessage(CrawlerManagerExample.CheckCrawlerRequest.class, this::onCheckCrawler)
                // terminate crawler
                .onMessage(TerminateCrawler.class, this::onTerminated)
                // list crawler
                .onMessage(CrawlerManagerExample.ListCrawlersRequest.class, r -> r.groupId.equals(groupId), this::onCrawlerList)
                .onMessage(CrawlerManagerExample.ParseAllRequest.class, r -> r.groupId.equals(groupId), this::onParseAll)
                .build();
    }

    private CrawlerGroupExample onPostStop() {
        getContext().getLog().info("CrawlerGroup {} stopped", groupId);
        return this;
    }
}
