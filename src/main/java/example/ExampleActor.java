package example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.PreRestart;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ExampleActor extends AbstractBehavior<String> {

    // 外部要建立這個actor時所需要的公開靜態方法
    public static Behavior<String> create() {
        return Behaviors.setup(ExampleActor::new);
    }


    // 該actor的建構式
    public ExampleActor(ActorContext<String> context) {
        super(context);
        context.getLog().info("GetterActor started");
    }



    // 接收到訊息時的處理，通常是再轉給內部的行為來處理
    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                // 如果收到文字訊息"start"時，會呼叫自己的start()方法
                .onMessageEquals("start", this::start)
                // 如果收到command(自定義class)
                // .onMessage(RequestCommand.class, this::onRequestCommand)

                // 在lifecycle的某些階段會有對應的行為
                // 參數為(type of signal to match, action to apply if the type matches) = (class[M], Function[M, Behavior[T]])
                .onSignal(PreRestart.class, signal -> preRestart())
                .onSignal(PostStop.class, signal -> postStop())
                .build();
    }

    private Behavior<String> preRestart() {
        System.out.println("second will be restarted");
        return this;
    }

    private Behavior<String> postStop() {
        System.out.println("second stopped");
        return this;
    }

    private Behavior<String> start() {
        ActorRef<String> firstRef = getContext().spawn(ExampleActor.create(), "first-actor");

        System.out.println("First: " + firstRef);
        firstRef.tell("printit");
        return Behaviors.same();
    }



    private ExampleActor onPostStop() {
        getContext().getLog().info("GetterActor stopped");
        return this;
    }
}
