package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
//@ConfigurationProperties("")
public class Config {
    @Value("${example.property}")

    private String exampleProperty;
    public String getExampleProperty(){
        return exampleProperty;
    }
}
