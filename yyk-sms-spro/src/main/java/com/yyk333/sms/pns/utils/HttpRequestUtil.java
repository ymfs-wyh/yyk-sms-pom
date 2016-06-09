package com.yyk333.sms.pns.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yyk333.sms.constants.SystemConstants;


/**
 * 
 * @Title: HttpRequestUtil.java  
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 上午10:24:58
 * @version V1.0
 */
public class HttpRequestUtil {
    
    private static Logger log = LoggerFactory.getLogger(HttpRequestUtil.class);
    
    
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return String (可以通过JSONUtil.toEntity(clazz, json)转换成相应的对象) 
     */  
    public static String httpRequestSSL(String requestUrl, String requestMethod, 
            String outputStr) {  
        return httpsRequest(requestUrl, requestMethod, outputStr, false);
    }
    
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return String (可以通过JSONUtil.toEntity(clazz, json)转换成相应的对象) 
     */  
    public static String httpsRequest(String requestUrl, String requestMethod, 
            String outputStr,boolean isBr) {  
        StringBuffer buffer = new StringBuffer();  
        try {  
        	System.setProperty("jsse.enableSNIExtension", "false");//jdk7不加有问题
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setConnectTimeout(Integer.parseInt(SystemConstants.HTTP_REQUEST_TIME_OUT));
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod)){                
                httpUrlConn.connect();  
            }
            
            log.debug("httpRequestSSL:"+outputStr);
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
                if(isBr){
                    buffer.append(SystemConstants.HTTP_RESPONSE_BR);
                }
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
        } catch (ConnectException ce) {  
            log.error("Weixin server connection timed out.");  
        } catch (SocketTimeoutException ste) {
            log.error("httpRequest-https request time out:{}",ste);  
            return "408";
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return buffer.toString();  
    }
    
    /**
     * 
      * getStringByUrl(根据url获取响应字符串)
      * @param url String输入的url
      * @return String 返回的响应
      * @throws Exception 异常
     */
    public static String getStringByUrl(String url) throws Exception{
        URL getUrl = new URL(url);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),"utf-8"));
        String lines;
        StringBuilder sb = new StringBuilder();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        
        return sb.toString();
    }
    
    /** 
     * 发起http请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return String (可以通过JSONUtil.toEntity(clazz, json)转换成相应的对象) 
     */  
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        StringBuffer buffer = new StringBuffer();  
        try {  
            log.debug("访问请求地址:"+requestUrl);
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.setConnectTimeout(Integer.parseInt(SystemConstants.HTTP_REQUEST_TIME_OUT));
            
            if ("GET".equalsIgnoreCase(requestMethod)){                
                httpUrlConn.connect();  
            }
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
        } catch (ConnectException ce) {  
            log.error("httpRequest-Weixin server connection timed out.",ce);  
        } catch (SocketTimeoutException ste) {
            log.error("httpRequest-https request time out:{}",ste);  
            return "408";
        } catch (Exception e) {  
            log.error("httpRequest-https request error:{}",e);  
        }  
        return buffer.toString();  
    }
    
}
