package ecommerce.producer;

import ecommerce.crawler.RutenCrawler;
import ecommerce.model.Category;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class AsyncCategoryProducer {

    private final KafkaProducer<String, Category> producer;
    private final String topic;
//    private final Boolean isAsync;
    public static final String BROKERLIST = "localhost:9092";
    public static final String CLIENT_ID = "CategoryProducer";

    public AsyncCategoryProducer(String topic) {
        this.topic = topic;
        this.producer = createProducer();

    }



    private void asyncSendToTopic(ProducerRecord<String, Category> record) {
        producer.send(record, new AsyncCategoryProducer.ProducerCallback());
    }

    private KafkaProducer<String, Category> createProducer() {
        ProducerConfigFactory configFactory = new CategoryProducerConfigFactory();
        Properties producerConfig = configFactory.createProducerConfig();
        producerConfig.setProperty(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        return new KafkaProducer<>(producerConfig);
    }

    private class ProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {

        }
    }
}
