package example.redis;

import redis.clients.jedis.Jedis;
import scala.concurrent.impl.FutureConvertersImpl;

public class Tutorial {
//    private static final String HOST = "127.0.0.1";
//    private static final String PORT = "6379";
    private static final Jedis jedis = new Jedis();


    public static void testCacheResponse() {

        jedis.set("test-key", "test-value");
        assert jedis.get("test-key").equals("test-value");
        jedis.del("test-key");
        assert jedis.get("test-key").equals(null);
    }

    public static void main(String[] args) {
        testCacheResponse();
    }
}
