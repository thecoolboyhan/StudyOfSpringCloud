package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//声明当前项目为Eureka服务
@EnableEurekaServer
public class EurekaServerApp8761 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp8761.class,args);
    }
}
