package com.teambind.messagesystem.handler;

import com.teambind.messagesystem.dto.Message;
import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.util.JsonUtil;
import jakarta.websocket.Session;

import java.io.IOException;

public class WebSocketSender {
	private final TerminalService terminalService;
	
	
	public WebSocketSender(TerminalService terminalService) {
		this.terminalService = terminalService;
	}
	
	public void sendMessage(Session session, Message message) {
		if (session != null && session.isOpen()) {
			JsonUtil.toJson(message).ifPresent(msg -> {
				try {
					session.getBasicRemote().sendText(msg);
				} catch (IOException e) {
					terminalService.printSystemMessage(String.format("Failed to send message to %s. Reason: %s", session.getId(), e.getMessage()));
				}
			});
		}
	}
}
