package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import crawler.WebClient;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * CrawlerActor has 3 behaviors: parse response body, get response body, update response body
 */
public class CrawlerActorExample extends AbstractBehavior<CrawlerActorExample.Command> {

    // state
    private final String groupId;
    private final String crawlerId;
    private final WebClient webClient = new WebClient();

    private Optional<String> result = Optional.empty();

    private CrawlerActorExample(ActorContext<Command> context, String groupId, String crawlerId) {
        super(context);
        this.groupId = groupId;
        this.crawlerId = crawlerId;
        context.getLog().info("Crawler actor {}-{} started", groupId, crawlerId);
    }

    public static Behavior<Command> create(String groupId, String crawlerId) {
        return Behaviors.setup(context -> new CrawlerActorExample(context, groupId, crawlerId));
    }

    public interface Command {}

    public static final class Parse implements Command {
        final String requestId;
        final String targetUrl;
        final ActorRef<Response>  replyTo;

        public Parse(String requestId, String targetUrl, ActorRef<Response> replyTo) {
            this.requestId = requestId;
            this.targetUrl = targetUrl;
            this.replyTo = replyTo;
        }
    }

    /**
     * 不直接使用CompletionStage的Callback是因為會引起存取到actor內部狀態的風險
     *
     * ref: https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html#send-future-result-to-self
     * */
    private Behavior<Command> onParse(Parse command) {
        CompletableFuture<String> futureResult = null;
        try {
            futureResult = webClient.getBodyWithCompletableFuture(command.targetUrl);
            getContext().pipeToSelf(
                    futureResult, (result, failure) -> {
//                        this.result = Optional.ofNullable(result);
//                        command.replyTo.tell(new ResponseHasParsed(command.requestId));
//                        return this;
                        return new Write(Optional.ofNullable(result), command.requestId, command.replyTo);
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
            return this;
        } catch (IllegalStateException e) {
            throw e;
        }
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

    public static class Read implements Command {
        final String requestId;
        final ActorRef<Response> replyTo;

        public Read(String requestId, ActorRef<Response> replyTo) {
            this.requestId = requestId;
            this.replyTo = replyTo;
        }
    }

    private Behavior<Command> onRead(Read command) {
        command.replyTo.tell(new Response(command.requestId, crawlerId, result));
        return this;
    }

    public static class Write implements Command {
        final Optional<String> result;
        final String requestId;
        public final ActorRef<Response> replyTo;

        public Write(Optional<String> result, String requestId, ActorRef<Response> replyTo) {
            this.result = result;
            this.requestId = requestId;
            this.replyTo = replyTo;
        }
    }

    private Behavior<Command> onWrite(Write command) {
        result = command.result;
        command.replyTo.tell(new Response(command.requestId, crawlerId, result));
        return this;
    }


    static enum Stop implements Command {
        INSTANCE
    }

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

    public static final class Response  {
        final String requestId;
        final String crawlerId;
        final Optional<String> result;

        public Response(String requestId, String crawlerId, Optional<String> result) {
            this.requestId = requestId;
            this.crawlerId = crawlerId;
            this.result = result;
        }
    }

    private Behavior<Command> onPostStop() {
        getContext().getLog().info("Crawler actor {}-{} stopped", groupId, crawlerId);
        return Behaviors.stopped();
    }


    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Parse.class, this::onParse)
                .onMessage(Read.class, this::onRead)
                .onMessage(Write.class, this::onWrite)
                .onMessage(Stop.class, message -> Behaviors.stopped())
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }
}
