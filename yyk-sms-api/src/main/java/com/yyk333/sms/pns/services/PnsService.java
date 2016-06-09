package com.yyk333.sms.pns.services;

public interface PnsService {

	/**
	 * 短息通知服务
	 * 
	 * @param mobiles：
	 *            手机号码列表，多个以英文逗号隔开
	 * @param content：
	 *            发送内容
	 * @return
	 */
	public String shortMsg_pns(String postData);

	/**
	 * 微信通知服务
	 * 
	 * @param openIds：
	 *            用户openID列表
	 * @param content：
	 *            发送内容
	 * @param templateId：
	 *            模板ID
	 * @param afterClickUrl：
	 *            点击消息后跳转Url
	 * @return
	 */
	public String weiXin_pns(String postData);

	/**
	 * 邮件通知服务
	 * 
	 * @param subject：
	 *            主题
	 * @param content：
	 *            发送内容
	 * @param receiveAddress：
	 *            接收人列表，多个以英文逗号隔开
	 * @param filePaths：
	 *            发送附件路径列表，多个以英文逗号隔开
	 * @return
	 */
	public String email_pns(String postData);

	/**
	 * App通知服务
	 * 
	 * @param appkey
	 * @param type
	 * @param payload
	 * @param productionMode
	 * @param description
	 * @param deviceTokens
	 * @param policy
	 * @param filter
	 * @param aliasType
	 * @param alias
	 * @param fileId
	 * @param thirdPartyId
	 * @param app_master_secret
	 * @return
	 */
	public String uMengApp_pnss(String postData);
	
	//public String webSocket_pnss(String post_data);
}
