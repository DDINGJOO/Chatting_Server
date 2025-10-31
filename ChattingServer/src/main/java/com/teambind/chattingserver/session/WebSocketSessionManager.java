package com.teambind.chattingserver.session;

import com.teambind.auth.entity.UserId;
import com.teambind.chattingserver.dto.Message;
import com.teambind.chattingserver.dto.websocket.inbound.BaseRequest;
import com.teambind.chattingserver.dto.websocket.outbound.BaseMessage;
import com.teambind.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
	private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);
	private final JsonUtil jsonUtil;
	private final Map<UserId, WebSocketSession> sessions = new ConcurrentHashMap<>();
	
	public WebSocketSessionManager(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}
	
	
	public List<WebSocketSession> getSessions() {
		return sessions.values().stream().toList();
	}
	
	public WebSocketSession getSession(UserId userId) {
		return sessions.get(userId);
	}
	
	public void putSession(UserId userId, WebSocketSession session) {
		log.info("Store Session : {}", session.getId());
		sessions.put(userId, session);
	}
	
	public void closeSession(UserId userId) {
		
		WebSocketSession webSocketSession = sessions.remove(userId);
		if (webSocketSession != null) {
			try {
				log.info("Terminate Session : {}", userId);
				webSocketSession.close();
				log.info("Session Closed : {}", userId);
			} catch (Exception e) {
				log.error("Fail WebsSocketSession close. userId: {}", userId);
			}
		}
	}
	
	
	public void sendMessage(WebSocketSession session, BaseMessage message) {
		jsonUtil.toJson(message).ifPresent(
				msg -> {
					try {
						session.sendMessage(new TextMessage(msg));
						log.info("sendMessage : {} to {}", msg, session.getId());
					} catch (Exception e) {
						log.error("메세지 발송 실패 cause : {} ", e.getMessage());
						
					}
				}
		);
	}
	
	public WebSocketSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	
}
