package com.teambind.common.config.auth;

import com.teambind.contents.Constants;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


@Component
public class WebSocketHandleShakeInterceptors extends HttpSessionHandshakeInterceptor {
	Logger log = LoggerFactory.getLogger(WebSocketHandleShakeInterceptors.class);
	
	
	
	
	@Override
	public boolean beforeHandshake(
			ServerHttpRequest request,
			ServerHttpResponse response,
			WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		
		if(request instanceof ServletServerHttpRequest servletServerHttpRequest)
		{
			HttpSession httpSession = servletServerHttpRequest.getServletRequest().getSession(false);
			if(httpSession != null)
			{
				attributes.put(Constants.HTTP_SESSION_ID.getValue(), httpSession.getId());
				return true;
			}
			else
			{
				log.info("Websocket Handshake Fai. cause : session is null");
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return false;
			}
		}
		else {
			log.info("Websocket Handshake Fai. request is {}", request.getClass());
			response.setStatusCode(HttpStatus.BAD_REQUEST);
			return false;
		}
	}
}
