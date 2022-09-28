package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//声明当前项目为Eureka服务
@EnableEurekaServer
public class EurekaServerApp8762 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp8762.class,args);
    }
}
