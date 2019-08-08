package com.spring.integration.controller;

import com.spring.integration.utils.PropertiesFileUtil;
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
//        RedisUtil.set("test","testvalue");
//        Demo demo = new Demo();
//        demo.setName("fuck");
//        log.info("demo = {}", demo);
        String str = PropertiesFileUtil.get("logging.level.root");
        System.out.println(str);
        return "success";
    }
}
