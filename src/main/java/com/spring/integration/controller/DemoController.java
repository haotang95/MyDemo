package com.spring.integration.controller;

import com.spring.integration.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/setValue")
    public String setValue(){
        new RedisUtil().set("test","testvalue");
        return "success";
    }
}
