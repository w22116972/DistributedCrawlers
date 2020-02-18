package kafkaexample.banktransaction;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kafkaexample.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class BankTransProducer {

    private static final Properties config = initializeConfig();

    public static void main(String[] args) {
        Producer<String, String> producer = new KafkaProducer<>(config);
        int i = 0;
        while (true) {
            System.out.println("Producing batch: " + i);
            try {
                producer.send(newRandomTrans("ander"));
                Thread.sleep(100);
                producer.send(newRandomTrans("banana"));
                Thread.sleep(100);
                producer.send(newRandomTrans("candy"));
                Thread.sleep(100);
                i += 1;
            } catch (InterruptedException e) {
                break;
            }
        }
        producer.close();
    }

    public static ProducerRecord<String, String> newRandomTrans(String name) {
        ObjectNode trans = JsonNodeFactory.instance.objectNode();
        Integer amount = ThreadLocalRandom.current().nextInt(0, 100);
        final Instant now = Instant.now();
        trans.put("name", name);
        trans.put("amount", amount);
        trans.put("time", now.toString());
        return new ProducerRecord<>("bank-trans", name, trans.toString());
    }

    private static Properties initializeConfig() {
        Properties producerConfig = ConfigFactory.createProducerConfig();
        producerConfig.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        producerConfig.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        producerConfig.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        producerConfig.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        return producerConfig;
    }
}
