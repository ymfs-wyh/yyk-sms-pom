
package com.yyk333.sms.constants;

import com.yyk333.sms.utils.PropertiesUtil;

/**
 * 系统常量
 * @author 魏彦浩 2016-4-18
 * @version V1.0
 */
public class SystemConstants {
	
	/**
     * http请求返回值换行进行标识区分
     */
    public static final String HTTP_RESPONSE_BR = "#;#"; 
    
    /**
     * http get请求方法
     */
    public static final String HTTP_REQUEST_METHOD_GET = "GET"; 
   
    /**
     * http post请求方法
     */
    public static final String HTTP_REQUEST_METHOD_POST = "POST"; 
    
    /**
     * http post请求超时设置
     */
    public static final String HTTP_REQUEST_TIME_OUT = PropertiesUtil.getProperty("http_request_time_out", "5000"); 
    
    /**
     * 通用分隔符，使用英文逗号
     */
    public static final String COMM_SPLIT = ",";
    
    /**
     * 字符编码：UTF-8
     */
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    /**
     * oss 文件服务器请求路径
     */
    public static final String FILE_SERVER_PATH = PropertiesUtil.getProperty("file_server_path", "http://yyk333-test-1.oss.yyk333.com/");
    
    /**
     * oss 图片上传位置
     */
    public static final String FILE_OSS_STORAGE_PATH = PropertiesUtil.getProperty("file_oss_storage_path", "Public/Uploads/");
    /**
     * 图片处理接口地址
     */
    public static final String PICTURE_DEAL_INTERFACE_ADDR = PropertiesUtil.getProperty("picture_deal_interface_addr", "http://yyk333-test-1.img-cn-beijing.aliyuncs.com/");
    
    /**
     * 本地文件临时存放位置
     */
    public static final String FILE_LOCAL_STORAGE_PATH = PropertiesUtil.getProperty("file_local_storage_path", "E:/home/images/");
    
    /**
     * memcache 缓存失效时间
     */
    // 启动图
    public static final String FIRING_MEMCACHE_EXPIRATION_TIME = PropertiesUtil.getProperty("firing_memcache_expiration_time", "30000");
    // 版本更新
    public static final String VERSION_UPDATE_MEMCACHE_EXPIRATION_TIME = PropertiesUtil.getProperty("version_update_memcache_expiration_time", "30000");
   
    public static final String RECOMMEND_CAT_ID = PropertiesUtil.getProperty("recommend_cat_id", "0");
    
    
        
}
