package com.teambind.auth.auth;

import com.teambind.auth.session.HttpSessionRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;
import java.util.Map;


public class CustomHandleShakeInterceptors extends HttpSessionHandshakeInterceptor {
	Logger log = LoggerFactory.getLogger(CustomHandleShakeInterceptors.class);
	
	private final HttpSessionRepository httpSessionRepository;
	
	
	public CustomHandleShakeInterceptors(HttpSessionRepository httpSessionRepository) {
		this.httpSessionRepository = httpSessionRepository;
	}
	
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		List<String> cookies =request.getHeaders().get("Cookie");
		if(cookies!=null)
		{
			for(String cookie : cookies)
			{
				if(cookie.startsWith("JSESSIONID"))
				{
					String sessionId = cookie.split("=")[1];
					HttpSession httpSession =  httpSessionRepository.findById(sessionId);
					if(httpSession != null)
					{
						log.info("Connected Session : {}", sessionId);
						return true;
					}
				}
			}
		}
		
		log.info("Unauthenticated access attempt : ClientId = {}", request.getRemoteAddress());
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return false;
	}
}
