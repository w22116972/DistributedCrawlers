package ecommerce.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class GenericConsumerConfigFactory<T>implements ConsumerConfigFactory {

    private static final String BROKERLIST = "localhost:9092";
    private Class<T> genericValueSerializerClass;

    public GenericConsumerConfigFactory(Class<T> genericValueSerializerClass) {
        this.genericValueSerializerClass = genericValueSerializerClass;
    }

    @Override
    public Properties createConsumerConfig() {
        Properties config = new Properties();
        config.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.genericValueSerializerClass.getName());
        config.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERLIST);
        return config;
    }
}
