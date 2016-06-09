package com.yyk333.sms.pns.umeng;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.pns.dto.UmengAppMsgInfo;
import com.yyk333.sms.pns.utils.HttpRequestUtil;
import com.yyk333.sms.utils.MD5Util;

/**
 * 
 * @Title: MailSenderUtil.java
 * Copyright: Copyright (c) 2015 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2015年5月25日 下午4:34:14
 * @version V1.0
 */
public class UmengAppPushUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(UmengAppPushUtil.class);
	private static final String UM_URL = "http://msg.umeng.com/api/send";
	/**
	 * @param umengAppMsgInfo
	 */
	public static String sendUmengAppMsg(UmengAppMsgInfo umengAppMsgInfo, String app_master_secret) {

		Properties pro = umengAppMsgInfo.getProperties();
		try{
			String postMsg = JSON.toJSONString(pro);
			String url = UM_URL + "?sign="+MD5Util.signUtf8(SystemConstants.HTTP_REQUEST_METHOD_POST+UM_URL+postMsg+app_master_secret);
			LOGGER.debug("友盟请求url:"+url);
			LOGGER.debug("友盟请求参数:"+postMsg);
	        String result = HttpRequestUtil.httpRequest(url,SystemConstants.HTTP_REQUEST_METHOD_POST, postMsg);
	        LOGGER.debug("友盟响应结果:"+result);
	        return result;
		} catch (Exception ex) {
			return "发送异常"+ex;
		}
		
	}
	
}