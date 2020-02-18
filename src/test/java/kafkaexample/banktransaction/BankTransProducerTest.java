package kafkaexample.banktransaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Test;

public class BankTransProducerTest {

    @Test
    public void testNewRandomTrans() {
        ProducerRecord<String, String> record = BankTransProducer.newRandomTrans("john");
        String key = record.key();
        String value = record.value();
        Assert.assertEquals(key, "john");
        ObjectMapper mapper = new ObjectMapper();
        try {
            final JsonNode jsonNode = mapper.readTree(value);
            Assert.assertEquals(jsonNode.get(("name")).asText(), "john");
            Assert.assertTrue(jsonNode.get("amount").asInt() < 100);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
