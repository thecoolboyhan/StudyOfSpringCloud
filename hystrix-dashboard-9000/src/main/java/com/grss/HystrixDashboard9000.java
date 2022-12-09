package com.grss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableEurekaClient
//开启hystrix的仪表盘
@EnableHystrixDashboard
public class HystrixDashboard9000 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboard9000.class,args);
    }
}
