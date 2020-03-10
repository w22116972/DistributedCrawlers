# Distributed Crawlers


## 104 spec

1. Kafka topic stores targets website

2. Producer/Actor
    - LinksCrawler: each crawler crawl all job links per page, and send links to load balancer
    - JobTextProcessor
3. load balancer (nginx) + cache (redis)
    - filter duplicate links
    - distribute links to worker/JobTextProcessor
4.

## 104 Pipeline

1. PageLinksProducer: send page links to topic
2.

each job des link selector
#js-job-content > article:nth-child(1) > div.b-block__left > h2 > a

---

## Tweet Stream Spec

1. twitter api in stream
2. akka stream/kafka stream for tweet
3. send processed tweet to nginx
4. nginx check if duplicate with redis
5. nginx send message to kafka topic based on compressed key
6. spring ui shows result

## Spec

- 設計queue存放待爬取url
    - 該queue可能是kafka cluster
- 設計load balancer (nginx) 分配url工作
    - 傳給queue或傳給kafka
- 設計cache存放已爬取url (redis)
    - 可能跟nginx放一起
- 設計Parser 以全文搜尋的方式(elasticsearch)把html page轉成有用的資訊並寫進kafka
<!-- - 設計extractor 以js interpreter engine解析動態內容 -->
- 設計mysql db for kafka cluster

## Pipeline

1. urls
2. load balancer and cache (nginx, redis)
3. kafka topic as message queue
4. es consume kafka topics
5. es produce processed message to other kafka topics
<!-- 6. use js interpret engine to extract dynamic content -->




---

Based on Akka

## Spec

- Ingest all inbound data into Kafka first, then consume it with the stream processors and microservices
- write data back to Kafka for consumption by downstream services
- When using direct connections between microservices, use libraries that implement the Reactive Streams standard
- Deploy to Kubernetes



---

## Kafka Stream vs Akka Stream

Kstream abstraction + KSQL

Akka Streams is more microservice oriented and less data-analytics oriented

---
