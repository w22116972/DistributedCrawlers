# Distributed Crawlers

Based on Akka



---

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
