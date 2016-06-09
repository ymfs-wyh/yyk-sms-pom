package com.yyk333.sms.pns.services.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.pns.dto.EmailMsgReturnDTO;
import com.yyk333.sms.pns.dto.MailSenderInfo;
import com.yyk333.sms.pns.dto.ResultTemplateMsg;
import com.yyk333.sms.pns.dto.ShortMsgReturnDTO;
import com.yyk333.sms.pns.dto.UmengAppMsgInfo;
import com.yyk333.sms.pns.dto.UmengAppMsgReturnDTO;
import com.yyk333.sms.pns.email.MailSenderUtil;
import com.yyk333.sms.pns.message.SmsMessageUtil;
import com.yyk333.sms.pns.services.PnsService;
import com.yyk333.sms.pns.umeng.UmengAppPushUtil;
import com.yyk333.sms.pns.weixin.WeiXinMessageUtil;

@Service("pnsService")
public class PnsServiceImpl implements PnsService {

	private static Logger logger = LoggerFactory.getLogger(PnsServiceImpl.class);

	@Override
	public String shortMsg_pns(String post_data) {
		logger.debug("推送服务控制器->短信通知服务：开始");
		logger.debug("推送服务控制器->短信通知服务入参->" + post_data);
		ShortMsgReturnDTO shortMsgReturnDTO = new ShortMsgReturnDTO();
		// 校验入参
		if (StringUtils.isNotBlank(post_data)) {// 参数非空

			@SuppressWarnings("unchecked")
			Map<String, Object> dataMap = JSON.parseObject(post_data, Map.class);

			String mobiles = (String) dataMap.get("mobiles");
			String content = (String) dataMap.get("content");

			if (StringUtils.isBlank(mobiles)) {
				// 手机号码或者发送内容为空时返回1000错误
				shortMsgReturnDTO.setMsgType("1001");
				shortMsgReturnDTO.setMsgContent("手机号码为空!");
				return JSON.toJSONString(shortMsgReturnDTO);
			}

			if (StringUtils.isBlank(content)) {
				// 手机号码或者发送内容为空时返回1000错误
				shortMsgReturnDTO.setMsgType("1002");
				shortMsgReturnDTO.setMsgContent("发送内容为空!");
				return JSON.toJSONString(shortMsgReturnDTO);
			}

			String result = SmsMessageUtil.sendChuangLanSmsMessage(mobiles, content);
			logger.debug("创蓝短信提交响应结果result：->" + result);

			if ("408".equals(result)) {
				shortMsgReturnDTO.setMsgType("408");
				shortMsgReturnDTO.setMsgContent("请求超时!");
			}

			String[] resArr = result.split(SystemConstants.COMM_SPLIT);
			shortMsgReturnDTO.setRespTime(resArr[0]);
			String tempStr = resArr[1];
			if (tempStr.startsWith("0")) {
				shortMsgReturnDTO.setRespStatus("0");
				shortMsgReturnDTO.setMsgId(tempStr.substring(1));
			} else {
				shortMsgReturnDTO.setRespStatus(tempStr);
			}

		} else {
			// 手机号码或者发送内容为空时返回1000错误
			shortMsgReturnDTO.setMsgType("1000");
			shortMsgReturnDTO.setMsgContent("手机号码或者发送内容为空!");
		}

		logger.debug("推送服务控制器->短信通知服务：结束");
		return JSON.toJSONString(shortMsgReturnDTO);
	}

	@Override
	public String weiXin_pns(String post_data) {
		logger.debug("推送服务控制器->微信通知服务：开始");
		logger.debug("推送服务控制器->微信通知服务->" + post_data);

		ResultTemplateMsg resultTemplateMsg = new ResultTemplateMsg();
		// 校验入参
		if (StringUtils.isNotBlank(post_data)) {// 参数非空

			resultTemplateMsg = WeiXinMessageUtil.sendWeiXinMessage(post_data);

			if (resultTemplateMsg == null) {
				resultTemplateMsg = new ResultTemplateMsg();
				resultTemplateMsg.setErrcode("-1");
				resultTemplateMsg.setErrmsg("发送微信消息异常");
			}

		} else {
			resultTemplateMsg.setErrcode("1");
			resultTemplateMsg.setErrmsg("请求参数为空...");
		}

		logger.debug("推送服务控制器->微信通知服务结果：" + resultTemplateMsg);
		logger.debug("推送服务控制器->微信通知服务：结束");
		return JSON.toJSONString(resultTemplateMsg);
	}

	@Override
	public String email_pns(String post_data) {

		logger.debug("推送服务控制器->邮件通知服务：开始");
		EmailMsgReturnDTO emailReturnDTO = new EmailMsgReturnDTO();
		if (StringUtils.isNotBlank(post_data)) {// 参数非空
			MailSenderInfo mailSenderInfo = JSON.parseObject(post_data, MailSenderInfo.class);
			String subject = mailSenderInfo.getSubject();
			String content = mailSenderInfo.getContent();
			String receiveAddress = mailSenderInfo.getReceiveAddress();
			String filePaths = mailSenderInfo.getFilePaths();
			String contentPicFilePaths = mailSenderInfo.getContentPicFilePaths();
			logger.debug("入参subject：->" + subject);
			logger.debug("入参content：->" + content);
			logger.debug("入参receiveAddress：->" + receiveAddress);
			logger.debug("入参filePaths：->" + filePaths);
			logger.debug("入参contentPicFilePaths：->" + contentPicFilePaths);
			// 校验入参
			if (StringUtils.isNotBlank(subject) && StringUtils.isNotBlank(receiveAddress)) {// 参数非空
				String[] filePathArr = null;
				if (StringUtils.isNotBlank(filePaths)) {
					filePathArr = filePaths.split(SystemConstants.COMM_SPLIT);
				}
				String[] contentPicFileArr = null;
				if (StringUtils.isNotBlank(contentPicFilePaths)) {
					contentPicFileArr = contentPicFilePaths.split(SystemConstants.COMM_SPLIT);
				}
				boolean result = MailSenderUtil.sendHtmlFileEmail(subject, content, receiveAddress, filePathArr,
						contentPicFileArr);
				logger.debug("邮件发送响应结果result：->" + result);
				if (result) {
					emailReturnDTO.setMsgType("0");
					emailReturnDTO.setMsgContent("邮件发送成功！");
				} else {
					emailReturnDTO.setMsgType("1");
					emailReturnDTO.setMsgContent("邮件发送失败！");
				}

			} else {
				emailReturnDTO.setMsgType("-1");
				emailReturnDTO.setMsgContent("邮件主题或者接收人为空！");
			}

		}
		logger.debug("推送服务控制器->邮件通知服务：结束");
		return JSON.toJSONString(emailReturnDTO);
	}

	@Override
	public String uMengApp_pnss(String post_data) {

		logger.debug("推送服务控制器->App通知服务：开始");
		UmengAppMsgReturnDTO umengAppMsgReturnDTO = new UmengAppMsgReturnDTO();
		if (StringUtils.isNotBlank(post_data)) {
			UmengAppMsgInfo umengAppMsgInfo = JSON.parseObject(post_data, UmengAppMsgInfo.class);
			try {
				logger.debug("推送服务控制器->App通知服务入参：" + umengAppMsgInfo.toString());
				String app_master_secret = umengAppMsgInfo.getAppMasterSecret();
				if (StringUtils.isNotBlank(app_master_secret)) {
					String result = UmengAppPushUtil.sendUmengAppMsg(umengAppMsgInfo, app_master_secret);
					logger.debug("UmengApp返回结果：" + result);

					if ("408".equals(result)) {
						umengAppMsgReturnDTO.setMsgType("408");
						umengAppMsgReturnDTO.setMsgContent("请求超时!");
					}

					umengAppMsgReturnDTO.setUmengReturn(result);
				} else {
					umengAppMsgReturnDTO.setMsgType("-1");
					umengAppMsgReturnDTO.setMsgContent("app_master_secret 不能为空!");
				}

				logger.debug("推送服务控制器->App通知服务执行结果" + umengAppMsgReturnDTO);
				logger.debug("推送服务控制器->App通知服务：结束");
			} catch (Exception e) {
				umengAppMsgReturnDTO.setMsgType("-1");
				umengAppMsgReturnDTO.setMsgContent("参数不能为空!");
				logger.debug("推送服务控制器->App通知服务执行异常，参数不能为空：" + e);
			}
		}
		return JSON.toJSONString(umengAppMsgReturnDTO);
	}

	/*@Override
	public String webSocket_pnss(String post_data) {
		
		logger.debug("WebSocket 通知服务开始->");
		
		if (StringUtils.isNotBlank(post_data)) {
			WebSocketMessage message = JSON.parseObject(post_data, WebSocketMessage.class);
			notificationHandler().sendMessageToUser(message.getId(), new TextMessage(message.getContent()));
			return "success";
		}
		return "failed";
	}
	
	@Bean
	public NotificationHandler notificationHandler() {
		return new NotificationHandler();
	}*/

}
