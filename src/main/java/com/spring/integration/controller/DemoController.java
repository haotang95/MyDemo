package com.spring.integration.controller;

import com.spring.integration.entity.Demo;
import com.spring.integration.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @GetMapping("/setValue")
    public String setValue(){
        RedisUtil.set("test","testvalue");
        Demo demo = new Demo();
        demo.setAge("asd");
        log.info("demo");
        return "success";
    }
}
