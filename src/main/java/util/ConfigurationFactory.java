package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationFactory {

    private static Properties config = null;

    public static Properties loadConfig() {
        if (config == null) {
            config = new Properties();
            try {
                File file = Paths.get("config.properties").toFile();
                assert file.exists();
                config.load(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}
