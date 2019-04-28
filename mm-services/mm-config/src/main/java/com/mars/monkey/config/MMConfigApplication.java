package com.mars.monkey.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created on 2018/1/11.
 * @author YouFeng.Zhu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class MMConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMConfigApplication.class, args);
    }
}






