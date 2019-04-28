package com.mars.monkey.user;

import com.mars.monkey.component.common.annotation.MMLibComponent;
import com.mars.monkey.security.annotation.MMResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created on 3/26/2019.
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.mars.monkey.user.dao.mapper")
@MMLibComponent
@MMResourceServer
public class MMUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MMUserApplication.class, args);
    }
}
