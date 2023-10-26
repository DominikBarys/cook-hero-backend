package com.barysdominik.tutorialservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableDiscoveryClient
public class TutorialServiceApplication {

    //TODO dodac tez delete, np page tego nie ma itp

    public static void main(String[] args) {
        SpringApplication.run(TutorialServiceApplication.class, args);
    }

}
