package com.teambind.messagesystem.handler;

import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.service.WebSocketService;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;

public class WebSocketSessionHandler extends Endpoint {
	
	private final TerminalService terminalService;
	private final WebSocketService webSocketService;
	
	public WebSocketSessionHandler(TerminalService terminalService, WebSocketService webSocketService) {
		this.terminalService = terminalService;
		this.webSocketService = webSocketService;
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
		webSocketService.closeSession();
		terminalService.printSystemMessage("Websocket error. Reason:" + thr.getMessage());
	}
}
