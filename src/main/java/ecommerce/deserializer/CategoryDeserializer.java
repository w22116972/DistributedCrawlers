package ecommerce.deserializer;

import ecommerce.model.Category;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

public class CategoryDeserializer implements Deserializer<Category> {

    private final static String DEFAULT_DECODE = "UTF-8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Category deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length < 8) {
            throw new SerializationException("Size of data received by deserializer is shorter than expected");
        }
        final ByteBuffer buffer = ByteBuffer.wrap(data);

        int lengthOfName = buffer.getInt();
        final byte[] nameBytes = new byte[lengthOfName];
        buffer.get(nameBytes);

        int lengthOfContent = buffer.getInt();
        final byte[] contentBytes = new byte[lengthOfContent];
        buffer.get(contentBytes);

        try {
            String name = new String(nameBytes, DEFAULT_DECODE);
            String content = new String(contentBytes, DEFAULT_DECODE);
            return new Category(name, Long.parseLong(content));
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error occur when deserialized!");
        }
    }

    @Override
    public void close() {

    }
}
