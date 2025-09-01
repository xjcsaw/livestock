package bag.livestock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LivestockApplication {

  public static void main(String[] args) {
    SpringApplication.run(LivestockApplication.class, args);
  }

}
