package bag.livestock.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the WebFlux backend.
 * This application demonstrates the advantages of Spring WebFlux for streaming
 * insurance damage statistics.
 */
@SpringBootApplication
@EnableScheduling
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }
}
