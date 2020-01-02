package util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.util.Map;

public class ConfigExample {
    public static void main(String[] args) {
        Config config = ConfigFactory.load(); //
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }
}
