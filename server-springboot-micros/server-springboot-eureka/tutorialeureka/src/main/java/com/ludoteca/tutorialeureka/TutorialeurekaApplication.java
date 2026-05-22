package com.ludoteca.tutorialeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TutorialeurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorialeurekaApplication.class, args);
    }
}
