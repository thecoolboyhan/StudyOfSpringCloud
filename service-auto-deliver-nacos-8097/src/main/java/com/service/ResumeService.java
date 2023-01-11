package com.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//配置当前类要访问的微服务名称,注册中心上的服务名称
@FeignClient(name = "service-resume")
@RequestMapping("/resume")
public interface ResumeService {

    @RequestMapping("/openState/{userId}")
    public String findDefaultResumeState(@PathVariable Long userId);

}
