package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EntityScan("com.pojo")
@EnableEurekaClient   //开启Eureka Client
//@EnableDiscoveryClient  //开启注册中心客户端    Eureka,Nacos都可以用这个注解
public class ResumeApplication8080 {

    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication8080.class,args);
    }
}
