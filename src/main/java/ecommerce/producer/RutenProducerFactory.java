package ecommerce.producer;

import ecommerce.serializer.CategorySerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class RutenProducerFactory implements ProducerFactory {

    private static final String BROKER = "localhost:9092";

    @Override
    public Producer createProducer() {
        Properties config = new Properties();
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CategorySerializer.class.getName());
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER);
        return new KafkaProducer<>(config);
    }

}
