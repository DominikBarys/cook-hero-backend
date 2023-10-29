package com.barysdominik.tutorialservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableDiscoveryClient
public class TutorialServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(TutorialServiceApplication.class, args);
    }

}
