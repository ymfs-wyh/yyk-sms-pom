package com.yyk333.sms.pns.dto;

/**
 * 创蓝短信提交响应返回
 * @author weiyanhao
 * @date 2016-04-21
 *
 */
public class ShortMsgReturnDTO extends MsgReturnDTO {
	/**
	 * 响应时间
	 */
	private String respTime;
	/**
	 * 响应状态
	 */
	private String respStatus;
	/**
	 * 消息Id
	 */
	private String msgId;
	
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}
	public String getRespStatus() {
		return respStatus;
	}
	public void setRespStatus(String respStatus) {
		this.respStatus = respStatus;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
}
