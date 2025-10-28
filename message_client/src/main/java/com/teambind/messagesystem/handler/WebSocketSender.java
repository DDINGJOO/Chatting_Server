package com.teambind.messagesystem.handler;

import com.teambind.messagesystem.dto.MessageRequest;
import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.util.JsonUtil;
import jakarta.websocket.Session;

public class WebSocketSender {
	private final TerminalService terminalService;
	
	
	public WebSocketSender(TerminalService terminalService) {
		this.terminalService = terminalService;
	}
	
	public void sendMessage(Session session, MessageRequest messageRequest) {
		if (session != null && session.isOpen()) {
			JsonUtil.toJson(messageRequest).ifPresent(payload -> {
				session.getAsyncRemote()
						.sendText(payload, result -> {
							if (!result.isOK()) {
								terminalService.printSystemMessage(String.format("Failed to send message %s. Reason: %s", payload, result.getException().getMessage()));
							}
						});
			});
		}
	}
}
