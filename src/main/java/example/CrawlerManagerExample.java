package example;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.*;

/**
 *
 *
 * CrawlerGroups and CrawlerActor are created on demand
 * CrawlerManager has registration protocol to register CrawlerActor and create CrawlerGroup, CrawlerActor
 * -> receive request with group Id and crawler Id
 *   -> group already has this crawler then forward request to this crawler
 *   -> or create new group with that crawler
 * -> receive request with registering crawler
 *   -> group already has this crawler then replies with ActorRef of existing crawler
 *   -> or group create new crawler and replies with ActorRef of new crawler
 * */

public class CrawlerManagerExample extends AbstractBehavior<CrawlerManagerExample.Request> {

    private final Map<String, ActorRef<CrawlerGroupExample.Command>> groupIdToCrawlerGroup = new HashMap<>();

    private CrawlerManagerExample(ActorContext<Request> context) {
        super(context);
        context.getLog().info("CrawlerManager started");
    }


    public static Behavior<Request> create() {
        return Behaviors.setup(CrawlerManagerExample::new);
    }

    public interface Command {}

    public interface Request {}

    public interface Response {}

    public static final class CheckCrawlerRequest implements Request, CrawlerGroupExample.Command {
        public final String groupId;
        public final String crawlerId;
        public final ActorRef<CheckCrawlerResponse> replyTo;

        public CheckCrawlerRequest(String groupId, String crawlerId, ActorRef<CheckCrawlerResponse> replyTo) {
            this.groupId = groupId;
            this.crawlerId = crawlerId;
            this.replyTo = replyTo;
        }
    }

    public static final class CheckCrawlerResponse implements Response {
        public final ActorRef<CrawlerActorExample.Command> crawlerRef;

        public CheckCrawlerResponse(ActorRef<CrawlerActorExample.Command> crawlerRef) {
            this.crawlerRef = crawlerRef;
        }
    }

    private CrawlerManagerExample onCheckCrawler(CheckCrawlerRequest command) {
        String groupId = command.groupId;
        ActorRef<CrawlerGroupExample.Command> ref = groupIdToCrawlerGroup.get(groupId);
        if (ref != null) {
            ref.tell(command);
        } else {
            getContext().getLog().info("Creating device group actor for {}", groupId);
            ActorRef<CrawlerGroupExample.Command> groupActor =
                    getContext().spawn(CrawlerGroupExample.create(groupId), "group-" + groupId);
            getContext().watchWith(groupActor, new TerminateGroupRequest(groupId));
            groupActor.tell(command);
            groupIdToCrawlerGroup.put(groupId, groupActor);
        }
        return this;
    }

    //
    public static final class ListCrawlersRequest implements Request, CrawlerGroupExample.Command {
        final long requestId;
        final String groupId;
        final ActorRef<ListCrawlersResponse> replyTo;

        public ListCrawlersRequest(long requestId, String groupId, ActorRef<ListCrawlersResponse> replyTo) {
            this.requestId = requestId;
            this.groupId = groupId;
            this.replyTo = replyTo;
        }
    }

    public static final class ListCrawlersResponse implements Response {
        final long requestId;
        final Set<String> ids;

        public ListCrawlersResponse(long requestId, Set<String> ids) {
            this.requestId = requestId;
            this.ids = ids;
        }
    }

    private CrawlerManagerExample onListCrawler(ListCrawlersRequest command) {
        ActorRef<CrawlerGroupExample.Command> ref = groupIdToCrawlerGroup.get(command.groupId);
        if (ref != null) {
            ref.tell(command);
        } else {
            command.replyTo.tell(new ListCrawlersResponse(command.requestId, Collections.emptySet()));
        }
        return this;
    }


    private static class TerminateGroupRequest implements Request {
        public final String groupId;
        TerminateGroupRequest(String groupId) {
            this.groupId = groupId;
        }
    }


    private CrawlerManagerExample onTerminateGroup(TerminateGroupRequest command) {
        getContext().getLog().info("Crawler group actor for {} has been terminated", command.groupId);
        groupIdToCrawlerGroup.remove(command.groupId);
        return this;
    }

    private CrawlerManagerExample onPostStop() {
        getContext().getLog().info("DeviceManager stopped");
        return this;
    }

    public static final class ParseAllRequest implements Request, CrawlerGroupReader.Command, CrawlerGroupExample.Command, Command {
        final String requestId;
        final String groupId;
        final List<String> targetUrls;
        final ActorRef<ParseAllResponse> replyTo;

        public ParseAllRequest(String requestId, String groupId, List<String> targetUrls, ActorRef<ParseAllResponse> replyTo) {
            this.requestId = requestId;
            this.groupId = groupId;
            this.targetUrls = targetUrls;
            this.replyTo = replyTo;
        }
    }

    public static final class ParseAllResponse {
        final String requestId;
        final Map<String, CrawlerState> crawlerIdToQueryState;

        public ParseAllResponse(String requestId, Map<String, CrawlerState> crawlerIdToQueryState) {
            this.requestId = requestId;
            this.crawlerIdToQueryState = crawlerIdToQueryState;
        }
    }

    private CrawlerManagerExample onParseAllTargets(ParseAllRequest command) {
        ActorRef<CrawlerGroupExample.Command> ref = groupIdToCrawlerGroup.get(command.groupId);
        if (ref != null) {
            ref.tell(command);
        } else {
            // 如果該command指定的group不存在，並不會主動建立該group，而是回傳`RespondAllTargets`訊息
            // 建立新group的工作是交給checkCrawlerRequest
            command.replyTo.tell(new ParseAllResponse(command.requestId, Collections.emptyMap()));
        }
        return this;
    }

    /*
    * 4 States for crawler
    * */
    public interface CrawlerState {}

    // url is available
    public static final class AvailableCrawler implements CrawlerState {
        public final String result;

        public AvailableCrawler(String result) {
            this.result = result;
        }
    }

    public enum TargetNotAvailable implements CrawlerState {
        INSTANCE
    }

    public enum FailedCrawler implements CrawlerState {
        INSTANCE
    }

    public enum Timeout implements CrawlerState {
        INSTANCE
    }

    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(ParseAllRequest.class, this::onParseAllTargets)
                .onMessage(CheckCrawlerRequest.class, this::onCheckCrawler)
                .onMessage(ListCrawlersRequest.class, this::onListCrawler)
                .onMessage(TerminateGroupRequest.class, this::onTerminateGroup)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }
}
