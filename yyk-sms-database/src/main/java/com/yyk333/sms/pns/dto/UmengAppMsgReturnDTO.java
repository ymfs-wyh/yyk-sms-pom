package com.yyk333.sms.pns.dto;

/**
 * 创蓝短信提交响应返回
 * @author weiyanhao
 * @date 2016-04-21
 *
 */
public class UmengAppMsgReturnDTO extends MsgReturnDTO {
	
	private String umengReturn;

	public String getUmengReturn() {
		return umengReturn;
	}

	public void setUmengReturn(String umengReturn) {
		this.umengReturn = umengReturn;
	}

	@Override
	public String toString() {
		return "UmengAppMsgReturnDTO [umengReturn=" + umengReturn + "]";
	}
	
}
