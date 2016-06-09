package com.yyk333.sms.pns.dto;

public class MsgReturnDTO {
	
	private String msgType;
	private String msgContent;

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Override
	public String toString() {
		return "MsgReturnDTO [msgType=" + msgType + ", msgContent=" + msgContent + "]";
	}
	
}
