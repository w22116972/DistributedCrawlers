package example.spring.ch1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication // tell SprintBootFramework that this class is entry point for Spring Boot Service
@RestController // tell SprintBootFramework that expose the code in this class as RestController class
@RequestMapping(value="hello") // all Urls expose in this app have prefix /hello
public class App {
//    public static void main(String[] args) {
////        SpringApplication.run(App.class, args);
//    }

    @GetMapping(value="/{firstName}")
    public String helloGET(
            @PathVariable("firstName") String firstName,
            @RequestParam("lastName") String lastName) {
        return String.format("{\"message\":\"Hello %s %s\"}",firstName, lastName);
    }

    @PostMapping // expose endpoint as POST-based
    public String helloPOST( @RequestBody HelloRequest request) { // receive Json of HelloRequest
        return String.format("{\"message\":\"Hello %s %s\"}",request.getFirstName(), request.getLastName());
    }


    class HelloRequest{

        private String firstName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
