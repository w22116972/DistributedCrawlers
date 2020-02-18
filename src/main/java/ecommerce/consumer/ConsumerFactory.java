package ecommerce.consumer;

import org.apache.kafka.clients.consumer.Consumer;

public interface ConsumerFactory {

    Consumer createConsumer();
}
