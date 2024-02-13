package com.vinta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = {"com.vinta.mapper"})
public class VintaMoonApplication {

    public static void main(String[] args) {
        SpringApplication.run(VintaMoonApplication.class, args);
    }

}
