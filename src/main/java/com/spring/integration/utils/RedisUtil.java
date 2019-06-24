package com.spring.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;


    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


}
