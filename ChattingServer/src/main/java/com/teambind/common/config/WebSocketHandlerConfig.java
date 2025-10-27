package com.teambind.common.config;

import com.teambind.chattingserver.handler.MessageHandler;
import com.teambind.common.config.auth.WebSocketHandleShakeInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {
	private final MessageHandler messageHandler;
	private final WebSocketHandleShakeInterceptors webSocketHandleShakeInterceptors;
	
	public WebSocketHandlerConfig(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		this.webSocketHandleShakeInterceptors = new WebSocketHandleShakeInterceptors();
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(messageHandler, "/ws/v1/message")
				.addInterceptors(webSocketHandleShakeInterceptors);
		
	}
}
