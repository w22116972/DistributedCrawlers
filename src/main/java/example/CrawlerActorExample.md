# CrawlerActor

client所用的是`interface Command`

傳遞物件的是`interface Response`

該actor的訊息種類及對應的行為
- 接收到`Parse`命令後會啟動`onParse`行為，並送出`Write`命令給自己並啟動`onWrite`行為(把結果寫入state)
- `Read`命令會啟動`onRead`行為，並回傳`ReadResponse`訊息
- `Stop` -> `Behaviors.stopped()`
