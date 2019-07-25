package com.spring.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName PropertiesFileUtil
 * @Author tangh
 * @Date 2019/7/25
 */
@Component
public class PropertiesFileUtil {

    private static PropertiesFileUtil propertiesFileUtil;

    @Autowired
    private Environment environment;

    public PropertiesFileUtil(){
        System.out.println(environment);
    }

//    @PostConstruct //创建对象后一定会执行一次 也只执行一次
//    public void init(){
//        propertiesFileUtil = this;
//        propertiesFileUtil.environment = this.environment;
//    }

    public static String get(String param){
        PropertiesFileUtil propertiesFileUtil = new PropertiesFileUtil();
        String property = propertiesFileUtil.environment.getProperty(param);
        return property;
    }

//    public static Environment getEnvironment(){
//        System.out.println(propertiesFileUtil);
//        System.out.println(propertiesFileUtil.environment);
//        return propertiesFileUtil.environment;
//    }

//    public static Environment getEnvironment(){
//        return
//    }
}
