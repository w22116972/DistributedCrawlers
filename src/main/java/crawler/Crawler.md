# Crawler


## CrawlerRouter
#### state
- pooIdToCrawlerPool

#### behavior
- create Pool
- parse pool
- set pool strategy

## CrawlerPool
#### state
- poolId
- pool
- strategy

#### behavior
- tell Parse to all CrawlerActor within pool
- setter for strategy

## CrawlerActor
#### state
- poolId
- result

#### behavior
- Parse url
- Write to Kafka (after parsing)
