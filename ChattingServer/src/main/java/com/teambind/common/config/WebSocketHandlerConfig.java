package com.teambind.common.config;

import com.teambind.chattingserver.handler.WebSocketHandler;
import com.teambind.common.config.auth.WebSocketHandleShakeInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketHandlerConfig implements WebSocketConfigurer {
	private final WebSocketHandler webSocketHandler;
	private final WebSocketHandleShakeInterceptors webSocketHandleShakeInterceptors;
	
	public WebSocketHandlerConfig(WebSocketHandler webSocketHandler) {
		this.webSocketHandler = webSocketHandler;
		this.webSocketHandleShakeInterceptors = new WebSocketHandleShakeInterceptors();
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/ws/v1/message")
				.addInterceptors(webSocketHandleShakeInterceptors);
		
	}
}
