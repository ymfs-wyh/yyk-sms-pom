package com.yyk333.sms.pns.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 
 * @Title: MyAuthenticator.java Copyright: Copyright (c) 2015 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2015年5月25日 下午4:30:21
 * @version V1.0
 */
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

}