package com.yyk333.sms.pns.constants;

import com.yyk333.sms.utils.PropertiesUtil;

/**
 * 微信常量
 * @author 魏彦浩 2016-4-18
 * @version V1.0
 */
public class WeiXinConstants {

	/**
	 * 微信appid
	 */
	public static final String APPID = PropertiesUtil.getProperty("wei_xin_appid");

	/**
	 * 微信SECRET
	 */
	public static final String SECRET = PropertiesUtil.getProperty("wei_xin_secret");

}
