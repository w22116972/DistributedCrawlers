# Kafka

## installation on Mac

1. `brew install zookeeper`
2. `brew install kafka`

## start service

#### Sol 1

1. `brew services start zookeeper`
2. `brew services start kafka`

#### Sol 2

1. `zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties`
2. `kafka-server-start /usr/local/etc/kafka/server.properties`

## Configuration

#### Zookeeper

`/usr/local/etc/zookeeper/zoo.cfg`
`/usr/local/etc/kafka/zookeeper.properties`

#### Kafka

`/usr/local/etc/kafka/server.properties`

uncomment `listeners=PLAINTEXT://:9092`

