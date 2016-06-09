package com.yyk333.sms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @Title: PropertiesUtil.java
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 下午2:24:26
 * @version V1.0
 */
public class PropertiesUtil {

    /** 默认的properties */
    private static final Properties DEFAULT_PROPERTIES;
    static {
        DEFAULT_PROPERTIES = getPropertiesByFileName("common.properties");
    }

    /**
     * 获取属性对应的值
     * 
     * @param property
     *            属性
     * @param defaultValue
     *            没找到值返回的默认值
     * @return property 对应的值
     */
    public static String getProperty(String property, String defaultValue) {
        return DEFAULT_PROPERTIES.getProperty(property, defaultValue);
    }

    /**
     * 获取属性对应的值
     * 
     * @param property
     *            属性
     * @return property 对应的值 ,or {@code null}
     */
    public static String getProperty(String property) {
        return DEFAULT_PROPERTIES.getProperty(property);
    }

    /**
     * 通过文件路径载入数据
     * 
     * @param fileName
     *            文件路径
     * @return {@link Properties}
     */
    public static Properties getPropertiesByFileName(String fileName) {
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(
                fileName);
        Properties p = new Properties();
        if (inputStream == null) {
            return p;
        }
        try {
            p.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return p;
    }

    /**
     * 通过属性获取指定文件的值
     * 
     * @param fileName 文件名
     * @param propertie 属性
     * @param defaultValue 默认值
     * @return 属性对应的值
     */
    public static String getProperty(String fileName, String propertie, String defaultValue) {
        Properties p = getPropertiesByFileName(fileName);
        return p.getProperty(propertie, defaultValue);
    }

}
