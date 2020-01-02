package crawler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import model.CrawlerRouterConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class CrawlerManager extends AbstractBehavior<CrawlerManager.Command> {

    // TODO: load from custom configuration
    private static final int POOL_SIZE = 5;

    private final Map<String, ActorRef<CrawlerRouter.Command>> routerIdToCrawlerRouter;

    private CrawlerManager(ActorContext<Command> context) {
        super(context);
        this.routerIdToCrawlerRouter = new HashMap<>();
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(context -> new CrawlerManager(context));
    }

    public interface Command {}

    public static final class CreateRouter implements Command, CrawlerRouter.Command {
        public final List<String> targetUrls;
        public final CrawlerRouterConfig routerConfig;
        public final ActorRef<CreateRouterResponse> replyTo;

        public CreateRouter(String poolId, List<String> targetUrls, int poolSize, PoolStrategy strategy, ActorRef<CreateRouterResponse> replyTo) {
            this.targetUrls = targetUrls;
            this.routerConfig = new CrawlerRouterConfig(poolId, poolSize, strategy);
            this.replyTo = replyTo;
        }
    }

    public static final class CreateRouterResponse {

    }

    public static final class StartRouter implements Command, CrawlerRouter.Command {
        public final String routerId;
        public final ActorRef<StartRouterResponse> replyTo;

        public StartRouter(String routerId, ActorRef<StartRouterResponse> replyTo) {
            this.routerId = routerId;
            this.replyTo = replyTo;
        }
    }

    public static final class StartRouterResponse {
        public final Optional<String> result;
        public final String targetUrl;
//        public final ParseState state;
        public final String routerId;

        public StartRouterResponse(Optional<String> result, String targetUrl, String routerId) {
            this.result = result;
            this.targetUrl = targetUrl;
//            this.state = state;
            this.routerId = routerId;
        }
    }

    private Behavior<Command> onStartRouter(StartRouter command) {
        if (!routerIdToCrawlerRouter.containsKey(command.routerId)) {
        } else {
            getContext().getLog().debug("Pool-{} doesn't exist", command.routerId);
        }
        return this;
    }

//    public interface ParseState {}
//
//    public static enum ParseSuccess implements ParseState {
//        INSTANCE
//    }
//
//    public static enum ParseFailed implements ParseState {
//        INSTANCE
//    }


//    public static final class SetPoolStrategy implements Command, CrawlerPool.Command {
//        public final String poolId;
//        public final PoolStrategy strategy;
//
//        public SetPoolStrategy(String poolId, PoolStrategy strategy) {
//            this.poolId = poolId;
//            this.strategy = strategy;
//        }
//    }

//    public static final class PoolConfig {
//
//
//        public PoolConfig(String poolId, int poolSize, PoolStrategy strategy) {
//            this.poolId = poolId;
//            this.poolSize = poolSize;
//            this.strategy = strategy;
//        }
//    }

    public enum PoolStrategy {ROUND_ROBIN, RANDOM, HASH}

    private Behavior<Command> onCreateRouter(CreateRouter command) {
        if (!routerIdToCrawlerRouter.containsKey(command.routerConfig.routerId)) {
            routerIdToCrawlerRouter.put(command.routerConfig.routerId, getContext().spawn(CrawlerRouter.create(command.routerConfig, command.targetUrls), command.routerConfig.routerId));
        } else {
            getContext().getLog().debug("PoolId-{} has already existed ", command.routerConfig.routerId);
        }
        return this;
    }

//    private Behavior<Command> onSetPoolStrategy(SetPoolStrategy command) {
//        ActorRef<CrawlerPool.Command> pool = poolIdToCrawlerPool.get(command.poolId);
//        if (pool != null) {
//            pool.tell(command);
//        } else {
//            getContext().getLog().debug("Pool-{} doesn't exist", command.poolId);
//        }
//        return this;
//    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(CreateRouter.class, this::onCreateRouter)
                .onMessage(StartRouter.class, this::onStartRouter)
                .build();
    }
}
