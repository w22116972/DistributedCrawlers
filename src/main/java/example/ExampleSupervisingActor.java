package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ExampleSupervisingActor extends AbstractBehavior<String> {
    static Behavior<String> create() {
        return Behaviors.setup(ExampleSupervisingActor::new);
    }

    private final ActorRef<String> child;

    private ExampleSupervisingActor(ActorContext<String> context) {
        super(context);
        child = context.spawn(

                Behaviors.supervise(ExampleSupervisingActor.create())
                        .onFailure(SupervisorStrategy.restart()),
                        "exampleChildActor");
    }

    @Override
    public Receive<String> createReceive() {
        //
        return newReceiveBuilder().onMessageEquals("tellChildFail", this::onFailChild).build();
    }

    private Behavior<String> onFailChild() {
        child.tell("fail");
        return this;
    }
}
