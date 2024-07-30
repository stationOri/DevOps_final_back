package org.example.oristationbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class OristationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OristationBackendApplication.class, args);
    }

    @PostConstruct
    public void changeTimeKST() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}