package ecommerce.producer;

import ecommerce.serializer.CategorySerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class GenericProducerConfigFactory<T> implements ProducerConfigFactory {

    private static final String BROKER = "localhost:9092";

    private Class<T> valueSerializerClass;

    public GenericProducerConfigFactory(Class<T> valueSerializer) {
        this.valueSerializerClass = valueSerializer;
    }

    @Override
    public Properties createProducerConfig() {
        Properties config = new Properties();
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass.getClass().getName());
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER);
        return config;
    }


}
