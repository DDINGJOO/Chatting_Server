package com.teambind.common.config.auth;

import com.teambind.auth.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketHandler webSocketHandler;
	private final WebSocketHandleShakeInterceptors webSocketHandleShakeInterceptors;
	
	public WebSocketConfig(WebSocketHandler webSocketHandler, WebSocketHandleShakeInterceptors webSocketHandleShakeInterceptors) {
		this.webSocketHandler = webSocketHandler;
		this.webSocketHandleShakeInterceptors = webSocketHandleShakeInterceptors;
		
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/ws/v1/connect")
				.addInterceptors(webSocketHandleShakeInterceptors);
	}
}
