package com.yyk333.sms.pns.dto;

import java.util.List;
import java.util.Properties;

/**
 * 
 * @Title: MailSenderInfo.java Copyright: Copyright (c) 2015 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2015年5月25日 下午4:35:38
 * @version V1.0
 */
public class MailSenderInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "25";
	// 邮件发送者的地址
	private String fromAddress;
	private String fromName;
	// 邮件接收者的地址
	private List<String> toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private List<String> attachFileNames;
	// 邮件附件的文件名
	private List<String> contentPicFileNames;
	
	// 邮件发送协议,默认stmp协议
	private String protocol = "stmp";
	// 邮件接收人地址字符串，多个英文逗号间隔
	private String receiveAddress;
	// 邮件发送附件路径
	private String filePaths;
	// 邮件发送内容图片路径
	private String contentPicFilePaths;
	// 是否是否启用调试模式
	private boolean isEnabledDebugMod = false;

	/**
	 * 获得邮件会话属性
	 */
	/**
	 * 获得邮件会话属性
	 */

	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.transport.protocol", this.protocol);
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		p.put("mail.mime.address.strict", "false");
		p.put("mail.debug",this.isEnabledDebugMod);
		return p;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public List<String> getToAddress() {
		return toAddress;
	}

	public void setToAddress(List<String> toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(List<String> attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(String filePaths) {
		this.filePaths = filePaths;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isEnabledDebugMod() {
		return isEnabledDebugMod;
	}

	public void setEnabledDebugMod(boolean isEnabledDebugMod) {
		this.isEnabledDebugMod = isEnabledDebugMod;
	}
	
	public String getFromName() {
		return fromName;
	}
	
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public List<String> getContentPicFileNames() {
		return contentPicFileNames;
	}

	public void setContentPicFileNames(List<String> contentPicFileNames) {
		this.contentPicFileNames = contentPicFileNames;
	}

	public String getContentPicFilePaths() {
		return contentPicFilePaths;
	}

	public void setContentPicFilePaths(String contentPicFilePaths) {
		this.contentPicFilePaths = contentPicFilePaths;
	}
	
}