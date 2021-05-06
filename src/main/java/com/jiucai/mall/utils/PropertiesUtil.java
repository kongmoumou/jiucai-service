package com.jiucai.mall.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(com.jiucai.mall.utils.PropertiesUtil.class);

    private static Properties properties;

    static{
        String fileName = "csumall.properties";
        properties = new Properties();
        try{
            properties.load(
                    new InputStreamReader(com.jiucai.mall.utils.PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        }catch (IOException e){
            logger.error("读取图片配置文件异常",e);
        }
    }

    public static String getProperty(String key){
        String value = properties.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }else{
            return value.trim();
        }
    }
}
