package com.yyk333.sms.pns.weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yyk333.sms.pns.dto.ResultTemplateMsg;

/**
 * 
 * @Title: WeiXinCoreUtil.java Copyright: Copyright (c) 2014 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2014-4-21 下午2:24:26
 * @version V1.0
 */
public class WeiXinMessageUtil {
	private static Logger logger = LoggerFactory.getLogger(WeiXinMessageUtil.class);

	/**
	 * 发送微信消息给用户
	 * 
	 * @param openIds
	 *            用户openID
	 * @param content
	 *            发送内容
	 * @param templateId
	 *            模板ID
	 * @param url
	 * @throws Exception
	 */
	public static ResultTemplateMsg sendWeiXinMessage(String dataJson) {

		logger.debug("发送微信消息开始data:" + dataJson);
		ResultTemplateMsg resultTemplateMsg = null;
		try {
			resultTemplateMsg = WeiXinURLUtil.sendTemplateMessageToUser(dataJson);
			logger.debug("微信模板消息发送状态：" + resultTemplateMsg.getErrcode() + ";信息：" + resultTemplateMsg.getErrmsg());
		} catch (Exception e) {
			logger.debug("发送微信消息异常:", e);
		}
		return resultTemplateMsg;

	}

}
