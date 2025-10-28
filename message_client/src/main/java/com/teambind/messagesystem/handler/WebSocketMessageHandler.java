package com.teambind.messagesystem.handler;

import com.teambind.messagesystem.dto.MessageRequest;
import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.util.JsonUtil;
import jakarta.websocket.MessageHandler;

public class WebSocketMessageHandler implements MessageHandler.Whole<String> {
	
	private final TerminalService terminalService;
	
	public WebSocketMessageHandler(TerminalService terminalService) {
		this.terminalService = terminalService;
	}
	
	
	@Override
	public void onMessage(String payload) {
		JsonUtil.fromJson(payload, MessageRequest.class).ifPresent(messageRequest -> {
			terminalService.PrintMessage(messageRequest.username(), messageRequest.content());
		});
		
	}
}
