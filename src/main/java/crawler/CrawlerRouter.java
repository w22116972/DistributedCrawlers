package crawler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.*;
import model.CrawlerRouterConfig;

import java.util.List;

public class CrawlerRouter extends AbstractBehavior<CrawlerRouter.Command> {
    private final String routerId;
    private final ActorRef<CrawlerActor.Command> router;
    private final List<String> targetUrls;


    private CrawlerRouter(ActorContext<Command> context, CrawlerRouterConfig config, List<String> targetUrls) {
        super(context);
        this.routerId = config.routerId;
        this.targetUrls = targetUrls;
        this.router = createRouter(config);
    }

    public static Behavior<Command> create(CrawlerRouterConfig config, List<String> targetUrls) {
        return Behaviors.setup(context -> new CrawlerRouter(context, config, targetUrls));
    }

    private ActorRef<CrawlerActor.Command> createRouter(CrawlerRouterConfig config) {
        PoolRouter<CrawlerActor.Command> poolRouter = Routers.pool(config.poolSize, Behaviors.supervise(CrawlerActor.create()).onFailure(SupervisorStrategy.restart()));
        switch (config.strategy) {
            case HASH:
                // TODO: implement consistent hashing
                break;
            case ROUND_ROBIN:
                poolRouter.withRoundRobinRouting();
                break;
            case RANDOM:
                poolRouter.withRandomRouting();
        }
        return getContext().spawn(poolRouter, config.routerId);
    }

    public interface Command {}

    // Behavior
    private CrawlerRouter onStartRouter(CrawlerManager.StartRouter command) {
        this.targetUrls.forEach(url ->
            this.router.tell(new CrawlerActor.StartCrawler(command.routerId, url, getContext().getSelf()));
        );
        return this;
    }

    public static final class StartCrawlerResponse {
        public final String result;
        public final String targetUrl;
        public final CrawlerActor.CrawlerState state;
        public final String routerId;

        public StartCrawlerResponse(String result, String targetUrl, CrawlerActor.CrawlerState state, String routerId) {
            this.result = result;
            this.targetUrl = targetUrl;
            this.state = state;
            this.routerId = routerId;
        }
    }

//    private Behavior<Command> onSetStrategy(CrawlerRouter.SetPoolStrategy command) {
//        switch (command.strategy) {
//            case HASH:
//                // TODO: implement consistent hashing
//                break;
//            case ROUND_ROBIN:
//                this.pool.withRoundRobinRouting();
//                break;
//            case RANDOM:
//                this.pool.withRandomRouting();
//        }
//        return this;
//    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CrawlerManager.StartRouter.class, this::onStartRouter)
                .build();
    }
}
