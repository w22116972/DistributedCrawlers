package ecommerce.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class AvroConsumerConfigFactory implements ConsumerConfigFactory {

    private final String registryUrl;
    private final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";

    public AvroConsumerConfigFactory(String registryUrl) {
        this.registryUrl = registryUrl;
    }

    @Override
    public Properties createConsumerConfig() {
        Properties config = new Properties();
        config.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        config.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        config.setProperty(SCHEMA_REGISTRY_URL_CONFIG, this.registryUrl);
        return config;
    }
}
