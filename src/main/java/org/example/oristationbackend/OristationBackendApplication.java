package org.example.oristationbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OristationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OristationBackendApplication.class, args);
    }

}
