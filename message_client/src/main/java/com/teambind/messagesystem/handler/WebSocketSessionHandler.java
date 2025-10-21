package com.teambind.messagesystem.handler;

import com.teambind.messagesystem.service.TerminalService;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;

public class WebSocketSessionHandler extends Endpoint {
	
	private final TerminalService terminalService;
	
	public WebSocketSessionHandler(TerminalService terminalService) {
		this.terminalService = terminalService;
	}
	
	@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		terminalService.printSystemMessage("Websocket connection established");
	}
	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		terminalService.printSystemMessage("Websocket connection closed. Reason:" + closeReason);
	}
	
	@Override
	public void onError(Session session, Throwable thr) {
		terminalService.printSystemMessage("Websocket error. Reason:" + thr.getMessage());
	}
}
