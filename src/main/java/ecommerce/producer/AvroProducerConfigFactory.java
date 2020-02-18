package ecommerce.producer;

import io.confluent.kafka.schemaregistry.client.rest.entities.Schema;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class AvroProducerConfigFactory implements ProducerConfigFactory {

    private final String registryUrl;
    private final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";

    public AvroProducerConfigFactory(String registryUrl) {
        this.registryUrl = registryUrl;
    }

    @Override
    public Properties createProducerConfig() {
        Properties config = new Properties();
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        config.setProperty(SCHEMA_REGISTRY_URL_CONFIG, this.registryUrl);
        return config;
    }
}
