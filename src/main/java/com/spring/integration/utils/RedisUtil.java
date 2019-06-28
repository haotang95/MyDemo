package com.spring.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 */
@Component
public class RedisUtil {

    private static RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
      * @Desciption: 因为该工具类中都是静态方法 直接调用静态方法 无法注入的redisTemplate
      * 用postConstruct初始化方法
      * 还有一种方法是将改工具栏注入到spring中 https://blog.csdn.net/zhanglei500038/article/details/86579021
      * @Author tangh
      * @Date 2019/6/28 11:45
      */
    @PostConstruct
    public void init(){
        redisUtil=this;
        redisUtil.redisTemplate = this.redisTemplate;
    }

    public static void set(String key, String value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    public static RedisTemplate getRedisTemplate(){
        return redisUtil.redisTemplate;
    }

}
