package ecommerce.producer;

import org.apache.kafka.clients.producer.Producer;

public interface ProducerFactory {

    Producer createProducer();

}
