package model;

import crawler.CrawlerManager;

public final class CrawlerRouterConfig {
    public final String routerId;
    public final int poolSize;
    public final CrawlerManager.PoolStrategy strategy;

    public CrawlerRouterConfig(String routerId, int poolSize, CrawlerManager.PoolStrategy strategy) {
        this.routerId = routerId;
        this.poolSize = poolSize;
        this.strategy = strategy;
    }
}
