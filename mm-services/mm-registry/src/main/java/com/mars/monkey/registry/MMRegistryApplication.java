package com.mars.monkey.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MMRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMRegistryApplication.class, args);
    }
}
