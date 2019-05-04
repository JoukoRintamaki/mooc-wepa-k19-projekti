package wall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class WallApplication {
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Helsinki"));
    }

    public static void main(String[] args) {
        SpringApplication.run(WallApplication.class, args);
    }
}



