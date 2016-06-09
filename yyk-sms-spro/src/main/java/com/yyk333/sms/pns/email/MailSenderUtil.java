package com.yyk333.sms.pns.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yyk333.sms.pns.constants.EmailConstants;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.pns.dto.MailSenderInfo;

/**
 * 
 * @Title: MailSenderUtil.java Copyright: Copyright (c) 2015 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2015年5月25日 下午4:34:14
 * @version V1.0
 */
public class MailSenderUtil {
	private static final Logger logger = LoggerFactory.getLogger(MailSenderUtil.class);

	/**
	 * sendHtmlFileEmail(发送附件HTML邮件)
	 * 
	 * @Title: sendHtmlFileEmail
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @return
	 * @return boolean true发送成功
	 */
	public static boolean sendHtmlFileEmail(String subject, String content, String receiveAddress, String[] filePaths, String[] contentPicFilePaths) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(EmailConstants.EMAIL_SERVER_HOST);
		mailInfo.setMailServerPort(EmailConstants.EMAIL_SERVER_PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(EmailConstants.EMAIL_SENDER_ADDRESS);
		mailInfo.setFromName(EmailConstants.EMAIL_SENDER_NAME);
		mailInfo.setPassword(EmailConstants.EMAIL_SENDER_PASSWORD);// 您的邮箱密码
		mailInfo.setFromAddress(EmailConstants.EMAIL_SENDER_ADDRESS);
		List<String> address = new ArrayList<String>();
		for (String str : receiveAddress.split(SystemConstants.COMM_SPLIT)) {
			address.add(str);
		}
		mailInfo.setToAddress(address);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);

		if (filePaths != null) {
			List<String> attachFileNames = new ArrayList<String>();
			// 此处是你要得到的上传附件的文件路径
			for (String filePath : filePaths) {
				attachFileNames.add(filePath);
			}
			mailInfo.setAttachFileNames(attachFileNames);
		}
		
		if (contentPicFilePaths != null) {
			List<String> attachFileNames = new ArrayList<String>();
			// 此处是你要得到的上传附件的文件路径
			for (String filePath : contentPicFilePaths) {
				attachFileNames.add(filePath);
			}
			mailInfo.setContentPicFileNames(attachFileNames);
		}

		try {
			// 采取html格式发送
			return sendMultipleEmail(mailInfo);
		} catch (UnsupportedEncodingException e) {
			logger.error("发送附件html格式的邮件异常", e);
		}
		return false;
	}

	/**
	 * 以HTML格式发送附件图文混合邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendMultipleEmail(MailSenderInfo mailInfo) throws UnsupportedEncodingException {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());

			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromName(),
					SystemConstants.CHARACTER_ENCODING);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);

			// 创建邮件的接收者地址，并设置到邮件消息中
			List<String> toAddress = null;
			if (mailInfo.getToAddress().size() > 1) {
				toAddress = mailInfo.getToAddress();
				Address[] address = new InternetAddress[toAddress.size()];
				for (int i = 0; i < toAddress.size(); i++) {
					address[i] = new InternetAddress(toAddress.get(i));
				}
				mailMessage.setRecipients(Message.RecipientType.TO, address);
			} else {
				toAddress = mailInfo.getToAddress();
				Address to = new InternetAddress(toAddress.get(0));
				// Message.RecipientType.TO属性表示接收者的类型为TO
				mailMessage.setRecipient(Message.RecipientType.TO, to);
			}

			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
			MimeMultipart mailContent = new MimeMultipart("mixed");
			mailMessage.setContent(mailContent);

			// 内容
			MimeBodyPart mailBody = new MimeBodyPart();
			mailContent.addBodyPart(mailBody);

			// 邮件正文(内嵌图片+html文本)
			MimeMultipart body = new MimeMultipart("related"); // 邮件正文也是一个组合体,需要指明组合关系
			mailBody.setContent(body);

			// 邮件正文:图文混排->由html和图片构成
			// 正文文字部分
			MimeBodyPart htmlPart = new MimeBodyPart();
			body.addBodyPart(htmlPart);
			// 图文内容："<span style='color:red'>这是我自己用java mail发送的邮件哦！ <img
			// src='cid:demo.png' /></span>"
			// 这里图片src的cid就是下边添加图片时的imgPart.setContentID(file.getName());
			MimeMultipart htmlMultipart = new MimeMultipart("alternative");
			htmlPart.setContent(htmlMultipart);
			MimeBodyPart htmlContent = new MimeBodyPart();
			htmlContent.setContent(mailInfo.getContent(), "text/html;charset=utf-8");
			htmlMultipart.addBodyPart(htmlContent);

			// 正文图片部分
			if (mailInfo.getContentPicFileNames() != null) {
				List<String> contentPicFileNames = mailInfo.getContentPicFileNames();
				for (String path : contentPicFileNames) {
					MimeBodyPart imgPart = new MimeBodyPart();
					body.addBodyPart(imgPart);
					File file = new File(path);
					DataSource ds = new FileDataSource(file);
					DataHandler dh = new DataHandler(ds);
					imgPart.setDataHandler(dh);
					imgPart.setContentID(MimeUtility.encodeText(file.getName()));
				}
			}

			// 添加附件
			if (mailInfo.getAttachFileNames() != null) {
				List<String> attachFileNames = mailInfo.getAttachFileNames();
				for (String path : attachFileNames) {
					MimeBodyPart attach = new MimeBodyPart();
					mailContent.addBodyPart(attach);
					File file = new File(path);
					DataSource ds = new FileDataSource(file);
					DataHandler dh = new DataHandler(ds);
					attach.setDataHandler(dh);
					attach.setFileName(MimeUtility.encodeText(file.getName()));
				}
			}

			// 保存邮件内容修改
			mailMessage.saveChanges();

			// 发送
			try {
				/* 这种方法发送有点小问题，邮件也可以使用以下方法发送
				 * Transport transport =
				 * sendMailSession.getTransport(mailInfo.getProtocol());
				 * transport.connect(mailInfo.getMailServerHost(),
				 * mailInfo.getUserName(), mailInfo.getPassword());
				 * transport.sendMessage(mailMessage,
				 * mailMessage.getAllRecipients()); transport.close();
				 */
				// 发送邮件
				Transport.send(mailMessage);
			} catch (Exception e) {
				logger.error("以HTML格式发送邮件失败", e);
				return false;
			}

			return true;
		} catch (MessagingException ex) {
			logger.error("以HTML格式发送邮件异常", ex);
		}
		return false;
	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			List<String> toAddress = null;
			if (mailInfo.getToAddress().size() > 1) {
				toAddress = mailInfo.getToAddress();
				Address[] address = new InternetAddress[toAddress.size()];
				for (int i = 0; i < toAddress.size(); i++) {
					address[i] = new InternetAddress(toAddress.get(i));
				}
				mailMessage.setRecipients(Message.RecipientType.TO, address);
			} else {
				toAddress = mailInfo.getToAddress();
				Address to = new InternetAddress(toAddress.get(0));
				// Message.RecipientType.TO属性表示接收者的类型为TO
				mailMessage.setRecipient(Message.RecipientType.TO, to);
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			/*
			 * // 发送邮件,注解的方法有点问题，现在发送邮件失败 Transport.send(mailMessage);
			 */

			try {
				Transport.send(mailMessage);
			} catch (Exception e) {
				logger.error("以HTML格式发送邮件失败", e);
				return false;
			}
			return true;
		} catch (MessagingException ex) {
			logger.error("以文本格式发送邮件异常", ex);
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) throws UnsupportedEncodingException {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			List<String> toAddress = null;
			if (mailInfo.getToAddress().size() > 1) {
				toAddress = mailInfo.getToAddress();
				Address[] address = new InternetAddress[toAddress.size()];
				for (int i = 0; i < toAddress.size(); i++) {
					address[i] = new InternetAddress(toAddress.get(i));
				}
				mailMessage.setRecipients(Message.RecipientType.TO, address);
			} else {
				toAddress = mailInfo.getToAddress();
				Address to = new InternetAddress(toAddress.get(0));
				// Message.RecipientType.TO属性表示接收者的类型为TO
				mailMessage.setRecipient(Message.RecipientType.TO, to);
			}

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			if (mailInfo.getAttachFileNames() != null) {
				List<String> attachFileNames = mailInfo.getAttachFileNames();
				for (String path : attachFileNames) {
					html = new MimeBodyPart();
					DataSource fds = new FileDataSource(path); // 得到数据源
					html.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
					// 此处是为了解决附件中文名乱码的问题
					String fileName = MimeUtility.encodeText(fds.getName());
					html.setFileName(fileName); // 得到文件名同样至入BodyPart
					mainPart.addBodyPart(html);
				}
			}
			SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
			mailMessage.setHeader("Date", format.format(new Date()));
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);

			/*
			 * // 发送邮件,注释的方法有点问题，现在发送邮件失败 Transport.send(mailMessage);
			 */

			try {
				Transport transport = sendMailSession.getTransport("smtp");
				transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
				transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
				transport.close();
			} catch (Exception e) {
				logger.error("以HTML格式发送邮件失败", e);
				return false;
			}

			return true;
		} catch (MessagingException ex) {
			logger.error("以HTML格式发送邮件异常", ex);
		}
		return false;
	}

}