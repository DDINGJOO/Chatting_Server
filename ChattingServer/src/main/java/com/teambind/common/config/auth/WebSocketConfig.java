package com.teambind.common.config.auth;

import com.teambind.auth.auth.CustomHandleShakeInterceptors;
import com.teambind.auth.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	private final WebSocketHandler webSocketHandler;
	private final CustomHandleShakeInterceptors customHandleShakeInterceptors;
	
	public WebSocketConfig(WebSocketHandler webSocketHandler, CustomHandleShakeInterceptors customHandleShakeInterceptors) {
		this.webSocketHandler = webSocketHandler;
		this.customHandleShakeInterceptors = customHandleShakeInterceptors;
		
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/ws/v1/connect")
				.addInterceptors(customHandleShakeInterceptors);
	}
}
