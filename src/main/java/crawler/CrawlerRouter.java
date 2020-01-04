package crawler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.*;
import model.CrawlerRouterConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrawlerRouter extends AbstractBehavior<CrawlerRouter.Command> {
    private final String routerId;
    private final ActorRef<CrawlerActor.Command> router;
    private final List<String> targetUrls;
    // no way that two messages could access an actor’s inner state concurrently and therefore it is perfectly fine to modify mutable state inside of an actor as a result of receiving a message.
    private final Map<String, String> results;

    private CrawlerRouter(ActorContext<Command> context, CrawlerRouterConfig config, List<String> targetUrls) {
        super(context);
        this.routerId = config.routerId;
        this.targetUrls = targetUrls;
        this.router = createRouter(config);
        this.results = new HashMap<>();
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
                break;
        }
        return getContext().spawn(poolRouter, config.routerId);
    }

    public interface Command {}

    // Behavior
    private CrawlerRouter onStartRouter(CrawlerManager.StartRouter command) {
        final ActorRef<CrawlerActor.StartCrawlerResponse> startCrawlerResponseAdapter = getContext().messageAdapter(CrawlerActor.StartCrawlerResponse.class, WrappedStartCrawlerResponse::new);

        this.targetUrls.forEach(url ->
            this.router.tell(new CrawlerActor.StartCrawler(command.routerId, url, startCrawlerResponseAdapter))
        );
        return this;
    }

    private static class WrappedStartCrawlerResponse implements Command {
        final CrawlerActor.StartCrawlerResponse response;

        public WrappedStartCrawlerResponse(CrawlerActor.StartCrawlerResponse response) {
            this.response = response;
        }
    }

//    public static final class StartCrawlerResponse {
//        public final String result;
//        public final String targetUrl;
//        public final CrawlerActor.CrawlerState state;
//        public final String routerId;
//
//        public StartCrawlerResponse(String result, String targetUrl, CrawlerActor.CrawlerState state, String routerId) {
//            this.result = result;
//            this.targetUrl = targetUrl;
//            this.state = state;
//            this.routerId = routerId;
//        }
//    }

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

    private Behavior<Command> onWrappedStartCrawlerResponse(WrappedStartCrawlerResponse r) {
        CrawlerActor.StartCrawlerResponse response = r.response;
        if (response.state instanceof CrawlerActor.ParseSuccess) {
            this.results.put(response.targetUrl, response.result);
        } else if (response.state instanceof CrawlerActor.TargetInvalid) {
            // TODO: log (url is invalid)
        } else if (response.state instanceof CrawlerActor.CrawlerFailure) {
            final ActorRef<CrawlerActor.StartCrawlerResponse> startCrawlerResponseAdapter = getContext().messageAdapter(CrawlerActor.StartCrawlerResponse.class, WrappedStartCrawlerResponse::new);
            this.router.tell(new CrawlerActor.StartCrawler(response.routerId, response.targetUrl, startCrawlerResponseAdapter));
        }
        return this;
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CrawlerManager.StartRouter.class, this::onStartRouter)
                .onMessage(WrappedStartCrawlerResponse.class, this::onWrappedStartCrawlerResponse)
                .build();
    }
}
