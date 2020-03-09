package util;

import ecommerce.consumer.GenericConsumerConfigFactory;
import org.apache.kafka.clients.consumer.Consumer;

public class TopicPrinter<T> {

    private final String topic;
    private static final String GROUP_ID = "printer";


//    private Consumer<String, T> consumer = new GenericConsumerConfigFactory<T>(T).createConsumerConfig();

    public TopicPrinter(String topic) {
        this.topic = topic;
    }

    public TopicPrinter(String topic, T deserializerClass) {
        this.topic = topic;

    }

    public void print() {

    }
}
