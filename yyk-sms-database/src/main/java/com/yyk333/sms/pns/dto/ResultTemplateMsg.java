package com.yyk333.sms.pns.dto;
/**
 * 封装提交post返回信息
 * @author 柴观新
 *
 */
public class ResultTemplateMsg extends ResultMsg{
	private String msgid;

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

}
