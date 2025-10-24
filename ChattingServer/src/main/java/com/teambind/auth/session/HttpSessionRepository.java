package com.teambind.auth.session;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HttpSessionRepository implements HttpSessionListener {
	private final Logger logger = LoggerFactory.getLogger(HttpSessionRepository.class);
	private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		sessions.remove(se.getSession().getId());
		logger.info("Session Destroyed : {}", se.getSession().getId());
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		sessions.put(se.getSession().getId(), (HttpSession) se.getSession());
		logger.info("Session Created : {}", se.getSession().getId());
	}
	
	
	public HttpSession findById(String sessionId) {
		return sessions.get(sessionId);
	}
	
	
}
