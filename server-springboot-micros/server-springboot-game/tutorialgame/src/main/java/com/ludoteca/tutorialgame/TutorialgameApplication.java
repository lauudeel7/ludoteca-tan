package com.ludoteca.tutorialgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TutorialgameApplication {

    public static void main(String[] args) {

        SpringApplication.run(TutorialgameApplication.class, args);
    }

}
