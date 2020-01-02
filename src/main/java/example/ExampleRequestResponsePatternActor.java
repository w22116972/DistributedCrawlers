package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Optional;

// Akka 採用 At-most-once delivery

public class ExampleRequestResponsePatternActor extends AbstractBehavior<ExampleRequestResponsePatternActor.Command> {

    private final String actorId;

    public static Behavior<Command> create(String actorId) {
        return Behaviors.setup(context -> new ExampleRequestResponsePatternActor(context, actorId));
    }

    private ExampleRequestResponsePatternActor(ActorContext<Command> context, String actorId) {
        super(context);
        this.actorId = actorId;
        context.getLog().info("Example actor {} started", actorId);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ReadRequest.class, this::onRequestCommand)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onRequestCommand(ReadRequest r) {
        r.replyTo.tell(new ReadResponseMessage(r.requestId, Optional.empty()));
        return this;
    }

    private ExampleRequestResponsePatternActor onPostStop() {
        getContext().getLog().info("Example actor {} stopped", actorId);
        return this;
    }

    // 取代原本的<String>，更抽象化
    public interface Command {}

    // Read Protocol: ReadCommand+ResponseMessage
    public static final class ReadRequest implements Command {
        final long requestId;
        final double value;
        final ActorRef<ReadResponseMessage> replyTo;

        public ReadRequest(long requestId, double value, ActorRef<ReadResponseMessage> replyTo) {
            this.requestId = requestId;
            this.value = value;
            this.replyTo = replyTo;
        }
    }

    public static final class ReadResponseMessage {
        final long requestId;
        final Optional<Double> value;

        public ReadResponseMessage(long requestId, Optional<Double> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }

    // Write Protocol: WriteCommand
    public static final class WriteRequest implements Command {
        final long requestId;
        final double value;
        final ActorRef<WriteResponseMessage> replyTo;

        public WriteRequest(long requestId, double value, ActorRef<WriteResponseMessage> replyTo) {
            this.requestId = requestId;
            this.value = value;
            this.replyTo = replyTo;
        }
    }

    public static final class WriteResponseMessage {
        final long requestId;

        public WriteResponseMessage(long requestId) {
            this.requestId = requestId;
        }
    }

}
