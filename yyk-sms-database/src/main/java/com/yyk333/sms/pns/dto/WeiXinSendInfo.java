package com.yyk333.sms.pns.dto;

public class WeiXinSendInfo {
	private String openIds;
	private String content;
	private String templateId;
	private String afterClickUrl;

	public String getOpenIds() {
		return openIds;
	}

	public void setOpenIds(String openIds) {
		this.openIds = openIds;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAfterClickUrl() {
		return afterClickUrl;
	}

	public void setAfterClickUrl(String afterClickUrl) {
		this.afterClickUrl = afterClickUrl;
	}

	@Override
	public String toString() {
		return "WeiXinSendInfo [openIds=" + openIds + ", content=" + content + ", templateId=" + templateId
				+ ", afterClickUrl=" + afterClickUrl + "]";
	}
	
}
