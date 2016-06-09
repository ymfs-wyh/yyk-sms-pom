package com.yyk333.sms.pns.weixin;

import com.yyk333.sms.pns.utils.AccessTokenUtil;
import com.yyk333.sms.pns.utils.HttpRequestUtil;
import com.alibaba.fastjson.JSON;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.pns.dto.AccessToken;
import com.yyk333.sms.pns.dto.ResultTemplateMsg;

/**
 * 
 * @Title: WeiXinURLUtil.java Copyright: Copyright (c) 2014 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2014-4-21 上午10:24:58
 * @version V1.0
 */
public class WeiXinURLUtil {

	/**
	 * 
	 * 主动发送模板消息到用户
	 * 
	 * @param postMessage
	 *            发送的消息字符串
	 * @throws Exception
	 *             异常
	 */
	public static ResultTemplateMsg sendTemplateMessageToUser(String postMessage) throws Exception {
		AccessToken accessToken = AccessTokenUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
				+ accessToken.getAccess_token();
		String result = HttpRequestUtil.httpRequestSSL(url, SystemConstants.HTTP_REQUEST_METHOD_POST, postMessage);
		
		if ("408".equals(result)) {
			ResultTemplateMsg templateMsg = new ResultTemplateMsg();
			templateMsg.setErrcode(result);
			templateMsg.setErrmsg("请求超时!");
			return templateMsg;
		}
		
		ResultTemplateMsg resultTemplateMsg = JSON.parseObject(result, ResultTemplateMsg.class);
		if ("0".equals(resultTemplateMsg.getErrcode())) {
			return resultTemplateMsg;
		} else {
			accessToken = AccessTokenUtil.getAccessTokenByUrl();
			AccessTokenUtil.saveAccessTokenToFile(accessToken);
			url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
					+ accessToken.getAccess_token();
			String result2 = HttpRequestUtil.httpRequestSSL(url, SystemConstants.HTTP_REQUEST_METHOD_POST, postMessage);
			return JSON.parseObject(result2, ResultTemplateMsg.class);
		}
	}

}
