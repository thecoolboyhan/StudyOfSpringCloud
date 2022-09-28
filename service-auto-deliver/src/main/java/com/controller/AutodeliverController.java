package com.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("autodeliver")
public class AutodeliverController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ResumeService resumeService;

//    @RequestMapping("checkState/{userId}")
//    public String findResumeName(@PathVariable Long userId){
//        //调用远程服务->Resume service
//        return "从远程调用的"+restTemplate.getForObject("http://127.0.0.1:8080/resume/openState/" + userId, String.class);
//    }

    /**
     * 服务注册到Eureka
     *
     * @param userId
     * @return
     */
//    @RequestMapping("checkState/{userId}")
//    public String findResumeName(@PathVariable Long userId){
//        //从Eureka Server中获取需要的服务实例
//        //使用客户端对象获取
//        List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-RESUME");
//        //如果有多个，选择使用一个（选择的过程就是负载均衡)
//        ServiceInstance serviceInstance = instances.get(0);
//        //3.从元数据信息获取host post
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//
//        return "从远程调用的"+restTemplate.getForObject("http://"+host+":"+port+"/resume/openState/" + userId, String.class);
//    }

    /**
     * 测试ribbon负载均衡
     * @param userId
     * @return
     */
//    @RequestMapping("checkState/{userId}")
//    public String findResumeName(@PathVariable Long userId){
//        //使用ribbon不需要我们自己获取服务实例
//        //指定要运行的服务名
//        String url="http://service-resume/resume/openState/"+userId;
//        return restTemplate.getForObject(url, String.class);
//    }

    /**
     * 使用Feign来调用远程接口
     * @param userId
     * @return
     */
    @RequestMapping("checkState/{userId}")
    public String findResumeName(@PathVariable Long userId){
        return resumeService.findDefaultResumeState(userId);
    }

    /**
     * 模拟熔断Hystrix,下面这种情况超过两秒不响应会自动抛出异常
     * @param userId
     * @return
     */
    //使用commandProperties进行熔断控制
//    @HystrixCommand(
//            //commandProperties熔断的细节属性配置
//            commandProperties = {
//                    //每个属性都是一个HystrixProperty
//                    @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout",value = "2000")
//            }
//    )
//    @RequestMapping("checkStateTimeout/{userId}")
//    public String findResumeNameTimeout(@PathVariable Long userId){
//        //使用ribbon不需要我们自己获取服务实例
//        //指定要运行的服务名
//        String url="http://service-resume/resume/openState/"+userId;
//        return restTemplate.getForObject(url, String.class);
//    }

    /**
     * Hystrix熔断，返回默认值
     * @param userId
     * @return
     */
    @HystrixCommand(
            //线程池标识，要保持唯一，不唯一的话就共用了
            threadPoolKey = "checkStateTimeout",
            //线程池细节配置
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize",value="1"),
                    @HystrixProperty(name="maxQueueSize",value = "20")//等待队列的长度
            },
            //commandProperties熔断的细节属性配置
            commandProperties = {
                    //每个属性都是一个HystrixProperty
                    @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout",value = "2000")
            },fallbackMethod = "myFallBack"//熔断的执行方法
    )
    @RequestMapping("checkStateTimeout/{userId}")
    public String findResumeNameTimeout(@PathVariable Long userId){
        //使用ribbon不需要我们自己获取服务实例
        //指定要运行的服务名
        String url="http://service-resume/resume/openState/"+userId;
        return restTemplate.getForObject(url, String.class);
    }


    //定义回退，返回阈值默认值
    //此方法形参和返回值与原始一致
    private String myFallBack(Long userId){
        return "熔断的备用数据";
    }

}
