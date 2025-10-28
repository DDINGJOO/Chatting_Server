package com.teambind.chattingserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SessionService {
	private final SessionRepository<? extends Session> httpSessionRepository;
	
	public SessionService(SessionRepository<? extends Session> httpSessionRepository) {
		this.httpSessionRepository = httpSessionRepository;
	}
	
	public void refreshTTL(String httpSessionId)
	{
		
		Session httpSession = httpSessionRepository.findById(httpSessionId);
		if(httpSession != null)
		{
			httpSession.setLastAccessedTime(Instant.now());
			
		}
		
	}
	
	public String getUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}
}
