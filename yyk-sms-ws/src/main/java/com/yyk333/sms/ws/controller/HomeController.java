package com.yyk333.sms.ws.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.yyk333.sms.ws.config.NotificationHandler;
import com.yyk333.sms.ws.model.User;

/**
 * 
 * @author Cliff
 */
@Controller
public class HomeController {

	@Bean
	public NotificationHandler notificationHandler() {
		return new NotificationHandler();
	}

	@RequestMapping(value = { "", "/home" }, method = RequestMethod.GET)
	public String home(String username, HttpSession session, Model model) {
		User user = new User();
		user.setUserId(System.currentTimeMillis());
		user.setUserName(username);
		session.setAttribute("uid", username);
		model.addAttribute("uid", username);
		return "test/index";
	}

	@RequestMapping(value = "/send")
	@ResponseBody
	public String send(String uid, String content, HttpServletResponse response) {
		notificationHandler().sendMessageToUser(uid, new TextMessage(content));
		return "success";
	}

}
