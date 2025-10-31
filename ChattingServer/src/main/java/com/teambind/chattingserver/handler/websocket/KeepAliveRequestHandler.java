package com.teambind.chattingserver.handler.websocket;

import com.teambind.chattingserver.dto.websocket.inbound.KeepAliveRequest;
import com.teambind.chattingserver.service.SessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class KeepAliveRequestHandler implements BaseRequestHandler<KeepAliveRequest>{
	private final SessionService sessionService;
	
	public KeepAliveRequestHandler(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Override
	public void handleRequest(WebSocketSession webSocketSession, KeepAliveRequest request) {
		sessionService.refreshTTL(webSocketSession.getId());
	}
}
