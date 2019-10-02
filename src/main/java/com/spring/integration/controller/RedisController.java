package com.spring.integration.controller;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.spring.integration.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 缓存穿透解决的
 * 1.引起缓存穿透:恶意请求一些不存在的数据 导致数据库压力变大 压垮数据库
 * 2.解决思路: 规范key的命名
 *            布隆过滤器校验key的真确性
 *            如果数据库没查到数据 也可以存个null到redis中 不至于频繁查询数据库 （null值需要设置过期时间）
 *            双重检查锁
 *
 */
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    /*示例规范key: authService:login:201901010112 服务或者系统:业务:id*/

    // 1M
    private static int size = 1 << 10 << 10 ;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);


    public static void main(String[] args) {
        System.out.println(size);
    }



    /**
     * 双重检查锁 + 数据库没有值的处理 + 布隆
     */
    @PostMapping("/getValue")
    public Object getValue(@PathVariable String key){
        //布隆校验
        if (!bloomFilter.mightContain(key.hashCode())){
            return null;
        }

        Object object = RedisUtil.getObject(key);
        log.info("读取缓存{}", object);
        if(object == null){
            synchronized(this){
                object = RedisUtil.getObject(key);
                if(object == null){
                    //查询数据库
                    object = "123";

                    //布隆
                    bloomFilter.put(key.hashCode());

                    //如果数据库查询不到值也可以存个空字符串进去
                    if(object == null){
                        RedisUtil.setObjectWithExprie(key, "", 1, TimeUnit.MINUTES);
                    }else{
                        RedisUtil.setObject(key, object);
                    }
                }
            }
        }
        return object;
    }
}
