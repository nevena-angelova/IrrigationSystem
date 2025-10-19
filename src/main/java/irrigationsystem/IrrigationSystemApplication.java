package irrigationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class IrrigationSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(IrrigationSystemApplication.class, args);
    }
}
