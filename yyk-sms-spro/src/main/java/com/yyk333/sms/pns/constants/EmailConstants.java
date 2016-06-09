package com.yyk333.sms.pns.constants;

import com.yyk333.sms.utils.PropertiesUtil;

/**
 * 邮件服务常量参数
 * 
 * @author 魏彦浩 2016-4-18
 * @version V1.0
 */
public class EmailConstants {

	/**
	 * 邮箱发件配置
	 */
	public static final String EMAIL_SERVER_HOST 		= PropertiesUtil.getProperty("email_server_host");
	public static final String EMAIL_SERVER_PORT 		= PropertiesUtil.getProperty("email_server_port");
	public static final String EMAIL_SENDER_ADDRESS 	= PropertiesUtil.getProperty("email_sender_address");
	public static final String EMAIL_SENDER_NAME 		= PropertiesUtil.getProperty("email_sender_name");
	public static final String EMAIL_SENDER_PASSWORD 	= PropertiesUtil.getProperty("email_sender_password");
	

}
