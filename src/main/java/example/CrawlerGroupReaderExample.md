# CrawlerGroupReader

以CrawlerGroupReader來代表不同query
* snapshot and IDs of active device actors to query
* ID of query request
* reference of the actor who sent the query
* timeout (Behaviors.withTimers)


* Q1. actor可能停止，且不會把結果回傳給Reader
    * 當拿到query請求時，groupActor會snapshot現存的所有actors，而且只會向當前這些actors要求資料
    * 如果在snapshot裡的actor沒有回應，就可以回報

* Q2. 新actor啟動時，Reader不知情
* A2. 會直接忽略在query請求後才啟動的actor

* Q3. actor處理太久
* A3. 增加timeout機制

## Command

- CrawlerTimeout
- WrappedResponse
    - wrap response from CrawlerActor
- CrawlerTerminated