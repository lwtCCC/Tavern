package com.lwt.tavern;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lwt.tavern.mapper")
//@SpringBootApplication
public class TavernApplication {

    public static void main(String[] args) {
        SpringApplication.run(TavernApplication.class, args);
    }

}
