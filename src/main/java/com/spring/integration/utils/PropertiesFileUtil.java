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

    @PostConstruct
    public void init(){
        propertiesFileUtil = this;
        propertiesFileUtil.environment = this.environment;
    }

    public String get(String param){
        String property = getEnvironment().getProperty(param);
        return property;
    }

    public Environment getEnvironment(){
        return propertiesFileUtil.environment;
    }
}
