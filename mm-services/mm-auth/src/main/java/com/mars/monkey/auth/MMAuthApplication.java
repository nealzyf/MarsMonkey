package com.mars.monkey.auth;

import com.mars.monkey.component.common.annotation.MMLibComponent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created on 3/26/2019.
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.mars.monkey.auth.dao.mapper")
@MMLibComponent
public class MMAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMAuthApplication.class, args);
    }
}
