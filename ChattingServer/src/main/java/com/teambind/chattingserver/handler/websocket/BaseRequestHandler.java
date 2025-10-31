package com.teambind.chattingserver.handler.websocket;

import com.teambind.chattingserver.dto.websocket.inbound.BaseRequest;
import org.springframework.web.socket.WebSocketSession;

public interface BaseRequestHandler<T extends BaseRequest> {
	void handleRequest(WebSocketSession webSocketSession, T request);

}
