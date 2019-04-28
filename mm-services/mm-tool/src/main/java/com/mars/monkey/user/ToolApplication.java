package com.mars.monkey.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created on 3/26/2019.
 */
@SpringBootApplication
//@EnableFeignClients
//@MapperScan("com.neal")
public class ToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class, args);
    }
}
