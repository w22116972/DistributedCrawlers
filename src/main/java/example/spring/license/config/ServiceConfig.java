package example.spring.license.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {
    @Value("${example.property}") // pulls the example.property from the Spring Cloud configuration server
    private String exampleProperty;

    public String getExampleProperty(){
        return exampleProperty;
    }
}
