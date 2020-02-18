package ecommerce.serializer;

import ecommerce.model.Category;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

public class CategorySerializer implements Serializer<Category> {

    private final static String DEFAULT_DECODE = "UTF-8";

    @Override
    public byte[] serialize(String topic, Category data) {
        if (data == null) {
            return null;
        } else {
            byte[] name;
            byte[] content;
            try {
                if (data.getName() != null) {
                    name = data.getName().getBytes(DEFAULT_DECODE);
                } else {
                    name = new byte[0];
                }
                if (data.getCount() != null) {
                    content =  String.valueOf(data.getCount()).getBytes(DEFAULT_DECODE);
                } else {
                    content = new byte[0];
                }
                ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + name.length + content.length);
                buffer.putInt(name.length);
                buffer.put(name);
                buffer.putInt(content.length);
                buffer.put(content);
                return buffer.array();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }
}
