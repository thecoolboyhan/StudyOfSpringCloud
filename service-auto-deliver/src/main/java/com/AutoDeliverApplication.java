package com;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//打开注册中心
@EnableDiscoveryClient
//打开Hystrix
//@EnableHystrix
//通用的打开熔断器功能
//@EnableCircuitBreaker
//一个注解覆盖上面的三个注解
@SpringCloudApplication
@EnableFeignClients//开启Feign客户端
public class AutoDeliverApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication.class,args);
    }

    //使用RestTemplate模板对象调用远程
    @Bean
    @LoadBalanced   //ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }



     // 在被监控的项目里引入此bean，通过访问此servlet来获取hystrix监控的数据
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet=new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("hystrixServlet");
        return registrationBean;
    }
}
