package com.yyk333.sms.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5签名处理核心文件
 * @Title: MD5.java
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-6-19 下午3:25:50
 * @version V1.0
 */

public class MD5Util {
    
    private static Logger log=LoggerFactory.getLogger(MD5Util.class);
    
	/**
     * 签名字符串  字符编码为UTF-8
     * @param text 需要签名的字符串
     * @param key 密钥
     * @return 签名结果
     */
    public static String signUtf8(String text) {
        MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			log.error("签名字符串signUtf8异常",e);
			return "";
		}

		byte[] md5Bytes = md5.digest(getContentBytes(text, "utf-8"));
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
    }
	/**
     * 签名字符串  字符编码为UTF-8
     * @param text 需要签名的字符串
     * @param key 密钥
     * @return 签名结果
     */
    public static String signUtf8(String text, String key) {
    	text = text+key;
        return signUtf8(text);
    }
    
    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String signAlipay(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verifyAlipay(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args) {
		System.out.println(signUtf8("888888"));
		
		Long cur = System.currentTimeMillis();
		System.out.println(cur);
		String s = cur.toString();
		System.out.println(s.substring(0, s.length()-3));
	}
}