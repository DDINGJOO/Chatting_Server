package com.teambind.auth.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
	Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.info("----Connected : {}", session.getId());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
		log.info("----Disconnected : {} ", session.getId());
	}
}
