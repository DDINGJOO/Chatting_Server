package com.teambind.chattingserver.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
	private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	
	
	public List<WebSocketSession> getSessions() {
		return sessions.values().stream().toList();
	}
	
	public void storeSession(WebSocketSession session) {
		log.info("Store Session : {}", session.getId());
		sessions.put(session.getId(), session);
	}
	
	public void terminateSession(String sessionId) {
		
		WebSocketSession webSocketSession = sessions.remove(sessionId);
		if (webSocketSession != null) {
			try {
				log.info("Terminate Session : {}", sessionId);
				webSocketSession.close();
				log.info("Session Closed : {}", sessionId);
			} catch (Exception e) {
				log.error("Fail WebsSocketSession close. sessionId: {}", e.getMessage());
			}
		}
		
	}
	
	public WebSocketSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	
}
