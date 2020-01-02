package crawler;

import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class GetterActor extends AbstractBehavior<Void> {

    // create GetterActor outside
    public static Behavior<Void> create() {
        return Behaviors.setup(GetterActor::new);
    }


    public GetterActor(ActorContext<Void> context) {
        super(context);
        context.getLog().info("GetterActor started");
    }



    //
    @Override
    public Receive<Void> createReceive() {

        return null;
    }



    private GetterActor onPostStop() {
        getContext().getLog().info("GetterActor stopped");
        return this;
    }
}
