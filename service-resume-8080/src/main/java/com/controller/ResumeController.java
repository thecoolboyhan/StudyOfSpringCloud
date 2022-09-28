package com.controller;

import com.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Value("${server.port}")
    private Integer port;
    @RequestMapping("/openState/{userId}")
    public String findDefaultResumeState(@PathVariable Long userId){

        //模拟超时
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        return resumeService.findDefaultResumeByUserId(userId).getName();
        return String.valueOf(port);
    }
}
