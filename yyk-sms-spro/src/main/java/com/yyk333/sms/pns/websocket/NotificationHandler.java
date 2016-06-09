package com.yyk333.sms.pns.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class NotificationHandler extends TextWebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(NotificationHandler.class);

	private static final Map<String, WebSocketSession> clients = new ConcurrentHashMap<String, WebSocketSession>();

	public NotificationHandler() {
	
	}

	/**
	 * 连接成功时候，会触发UI上onopen方法
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("连接成功...");
		// 这块会实现自己业务...
		clients.put(String.valueOf(session.getAttributes().get("userId")), session);

	}

	/**
	 * 在UI在用js调用websocket.send()时候，会调用该方法
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// super.handleTextMessage(session, message);

		sendMessageToUsers(message);
	}

	/**
	 * 给某个用户发送消息
	 *
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userId, TextMessage message) {
		for (WebSocketSession session : clients.values()) {
			if (userId.equals(String.valueOf(session.getAttributes().get("userId")))) {
				if (session.isOpen()) {
					try {
						session.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					clients.remove(session.getAttributes().get("userId"));
				}
				break;
			}
		}
	}

	/**
	 * 给所有在线用户发送消息
	 *
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
		try {
			for (WebSocketSession session : clients.values()) {
				if (session.isOpen()) {
					session.sendMessage(message);
				} else {
					clients.remove(session.getAttributes().get("userId"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		clients.remove(session);
		logger.debug("连接关闭...");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("连接关闭后执行...");
		clients.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
