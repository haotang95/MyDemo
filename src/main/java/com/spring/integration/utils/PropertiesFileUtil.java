package com.spring.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

//    @PostConstruct //创建对象后一定会执行一次 也只执行一次
    public PropertiesFileUtil(){
        propertiesFileUtil = this;
        propertiesFileUtil.environment = this.environment;
    }

    public static String get(String param){
        return getEnvironment().getProperty(param);
    }

    public static Environment getEnvironment(){
        return propertiesFileUtil.environment;
    }

}
