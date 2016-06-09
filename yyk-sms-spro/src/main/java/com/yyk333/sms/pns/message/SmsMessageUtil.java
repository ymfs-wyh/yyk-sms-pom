package com.yyk333.sms.pns.message;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yyk333.sms.pns.constants.SmsConstants;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.pns.utils.HttpRequestUtil;

public class SmsMessageUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsMessageUtil.class);

	/**
	 * 创蓝发送短信信息
	 * 
	 * @param mobiles
	 *            手机列表，用短号隔开
	 * @param content
	 *            发送内容
	 */
	public static String sendChuangLanSmsMessage(String mobiles, String content) {
		try {
			content = URLEncoder.encode(content, "UTF-8");
			String jsonResp = HttpRequestUtil.httpRequest(MessageFormat.format(SmsConstants.SMS_URL, mobiles, content),
					SystemConstants.HTTP_REQUEST_METHOD_GET, null);
			LOGGER.debug("返回值：" + jsonResp);
			return jsonResp;
		} catch (Exception e) {
			LOGGER.error("调用创蓝移动发送短信异常：", e);
		}
		return null;
	}

}
