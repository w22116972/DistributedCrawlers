package example;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.PreRestart;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ExampleChildActor extends AbstractBehavior<String> {

    static Behavior<String> create() {
        return Behaviors.setup(ExampleChildActor::new);
    }

    private ExampleChildActor(ActorContext<String> context) {
        super(context);
        System.out.println("supervised actor started");
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("fail", this::fail)
                .onSignal(PreRestart.class, signal -> preRestart())
                .onSignal(PostStop.class, signal -> postStop())
                .build();
    }

    private Behavior<String> fail() {
        System.out.println("supervised actor fails now");
        throw new RuntimeException("I failed!");
    }

    private Behavior<String> preRestart() {
        System.out.println("second will be restarted");
        return this;
    }

    private Behavior<String> postStop() {
        System.out.println("second stopped");
        return this;
    }
}
