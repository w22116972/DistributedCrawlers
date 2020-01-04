package crawler;

import akka.actor.Actor;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * CrawlerActor has 3 behaviors: parse response body, get response body, update response body
 */
public class CrawlerActor extends AbstractBehavior<CrawlerActor.Command> {

    // state
    private final WebClient webClient = new WebClient();
    private Optional<String> result = Optional.empty();
    private CrawlerActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> new CrawlerActor(context));
    }

    public interface Command {}

    public static final class StartCrawler implements Command {
        public final String routerId;
        public final String targetUrl;
        public final ActorRef<StartCrawlerResponse> replyTo;

        public StartCrawler(String routerId, String targetUrl, ActorRef<StartCrawlerResponse> replyTo) {
            this.routerId = routerId;
            this.targetUrl = targetUrl;
            this.replyTo = replyTo;
        }
    }

    public static final class StartCrawlerResponse {
        public final String routerId;
        public final String targetUrl;
        public final CrawlerState state;
        public final String result;

        public StartCrawlerResponse(String routerId, String targetUrl, CrawlerState state, String result) {
            this.routerId = routerId;
            this.targetUrl = targetUrl;
            this.state = state;
            this.result = result;
        }
    }

    public interface CrawlerState {}

    public static enum ParseSuccess implements CrawlerState {
        INSTANCE;

        @Override
        public String toString() {
            return "ParseSuccess";
        }
    }

    public static enum TargetInvalid implements CrawlerState {
        INSTANCE;

        @Override
        public String toString() {
            return "TargetInvalid";
        }
    }

    public static enum CrawlerFailure implements CrawlerState {
        INSTANCE;

        @Override
        public String toString() {
            return "CrawlerFailure";
        }
    }


    /**
     * 不直接使用CompletionStage的Callback是因為會引起存取到actor內部狀態的風險
     *
     * ref: https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html#send-future-result-to-self
     * */
    private Behavior<Command> onStartCrawler(StartCrawler command) {
        if (!WebClient.isValidURL(command.targetUrl)) {
            getContext().getLog().debug("Url is not valid: {}", command.targetUrl);
            command.replyTo.tell(new StartCrawlerResponse(command.routerId, command.targetUrl, TargetInvalid.INSTANCE, null));
//            return new CrawlFailure(command.routerId, command.targetUrl, , command.replyTo);
        } else {
            CompletableFuture<String> futureResult = webClient.getBodyWithCompletableFuture(command.targetUrl);
            getContext().pipeToSelf(
                    futureResult, (result, failure) -> {
                        if (failure == null && result != null) {
                            return new CrawlSuccess(command.routerId, command.targetUrl, result, command.replyTo);
                        } else {
                            getContext().getLog().debug("Raise exception: : {}", failure.getMessage());
                            return new CrawlFailure(command.routerId, command.targetUrl, failure, command.replyTo);
                        }
                        // TODO:implement write command
//                        return new Write(Optional.ofNullable(result), command.routerId, command.replyTo);

//                        this.result = Optional.ofNullable(result);
//                        command.replyTo.tell(new ResponseHasParsed(command.requestId));
//                        return this;

//                        if (result != null) {
////                            String filteredResult = command.filter.apply(result);
////                            lastResponseBody = Optional.ofNullable(result);
//                            return new ParseResponse(new ParseSuccess(command.requestId, crawlerId, result), command.replyTo);
//                            //command.replyTo.tell(new ParseResponse(command.requestId, crawlerId, lastGetResponseBody));
//                        } else {
//                            return new ParseResponse(new FailedParse(command.requestId, crawlerId, new RuntimeException(failure)), command.replyTo);
////                        throw new RuntimeException(failure);
//                        }
                    }
            );
        }
        return this;
    }

    public static final class CrawlResponse implements CrawlerActor.Command {
        public final String routerId;
        public final String targetUrl;
        public final String result;
        public final CrawlerState state;
//        public final ActorRef<StartCrawlerResponse> replyTo;

        public CrawlResponse(String routerId, String targetUrl, String result, CrawlerState state) {
            this.routerId = routerId;
            this.targetUrl = targetUrl;
            this.result = result;
            this.state = state;
        }
    }

    public static class CrawlSuccess implements CrawlerActor.Command {
        public final String routerId;
        public final String targetUrl;
        public final String result;
        public final ActorRef<StartCrawlerResponse> replyTo;

        public CrawlSuccess(String routerId, String targetUrl, String result, ActorRef<StartCrawlerResponse> replyTo) {
            this.routerId = routerId;
            this.targetUrl = targetUrl;
            this.result = result;
            this.replyTo = replyTo;
        }
    }

    private Behavior<Command> onCrawlSuccess(CrawlSuccess command) {
        command.replyTo.tell(new StartCrawlerResponse(command.routerId, command.targetUrl, ParseSuccess.INSTANCE, command.result));
        return this;
    }

    public static class CrawlFailure implements CrawlerActor.Command {
        public final String routerId;
        public final String targetUrl;
        public final Throwable exception;
        public final ActorRef<StartCrawlerResponse> replyTo;

        public CrawlFailure(String routerId, String targetUrl, Throwable exception, ActorRef<StartCrawlerResponse> replyTo) {
            this.routerId = routerId;
            this.targetUrl = targetUrl;
            this.exception = exception;
            this.replyTo = replyTo;
        }
    }

    private Behavior<Command> onCrawlFailure(CrawlFailure command) {
        command.replyTo.tell(new StartCrawlerResponse(command.routerId, command.targetUrl, CrawlerFailure.INSTANCE, null));
        return this;
    }


//    public static final class ParseResponse {
//        final String requestId;
//        final String crawlerId;
//        final Optional<String> responseBody;
//
//        public ParseResponse(String requestId, String crawlerId, Optional<String> responseBody) {
//            this.requestId =requestId;
//            this.crawlerId = crawlerId;
//            this.responseBody = responseBody;
//        }
//    }

//    public static class Read implements Command {
//        final String requestId;
//        final ActorRef<Response> replyTo;
//
//        public Read(String requestId, ActorRef<Response> replyTo) {
//            this.requestId = requestId;
//            this.replyTo = replyTo;
//        }
//    }
//
//    private Behavior<Command> onRead(Read command) {
//        command.replyTo.tell(new Response(command.requestId, result));
//        return this;
//    }

    // TODO: Write to Kafka
    public static class Write implements Command {
        final Optional<String> result;
        final String requestId;
        public final ActorRef<WriteResponse> replyTo;

        public Write(Optional<String> result, String requestId, ActorRef<WriteResponse> replyTo) {
            this.result = result;
            this.requestId = requestId;
            this.replyTo = replyTo;
        }
    }

    private Behavior<Command> onWrite(Write command) {
        result = command.result;
        command.replyTo.tell(new WriteResponse(command.requestId, result));
        return this;
    }

    public static final class WriteResponse {
        final String requestId;
        //        final String crawlerId;
        final Optional<String> result;
        public WriteResponse(String requestId, Optional<String> result) {
            this.requestId = requestId;
//            this.crawlerId = crawlerId;
            this.result = result;
        }
    }
//
//
//    static enum Stop implements Command {
//        INSTANCE
//    }

//    public static class ParseResponse implements Response {
//        public final Optional<String> result;
//        public ParseResponse(Optional<String> result) {
//            this.result = result;
//        }
//    }

//    interface ParseResponse {}
//
//    public static class ParseSuccess implements ParseResponse {
//        public final String id;
//        public final Optional<String> result;
//
//        public ParseSuccess(String id, Optional<String> result) {
//            this.id = id;
//            this.result = result;
//        }
//    }
//
//    public static class ParseFailure implements ParseResponse {
//        public final String id;
//        public final String reason;
//
//        public ParseFailure(String id, String reason) {
//            this.id = id;
//            this.reason = reason;
//        }
//    }


//    interface Response {}

//    public static final class ResponseHasParsed implements Response {
//        final String requestId;
//        public ResponseHasParsed(String requestId) {
//            this.requestId = requestId;
//        }
//    }



//    private Behavior<Command> onPostStop() {
////        getContext().getLog().info("Crawler actor from pool-{} stopped", poolId);
//        return Behaviors.stopped();
//    }




    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CrawlerActor.StartCrawler.class, this::onStartCrawler)
                .onMessage(CrawlerActor.CrawlSuccess.class, this::onCrawlSuccess)
                .onMessage(CrawlerActor.CrawlFailure.class, this::onCrawlFailure)
//                .onMessage(Read.class, this::onRead)
                .onMessage(Write.class, this::onWrite)
//                .onMessage(Stop.class, message -> Behaviors.stopped())
//                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }
}
