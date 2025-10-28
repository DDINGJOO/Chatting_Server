package com.teambind.chattingserver.service;


import com.teambind.auth.entity.MessageUserEntity;
import com.teambind.auth.entity.UserId;
import com.teambind.auth.repository.MessageUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageUserService {
	private final Logger log = LoggerFactory.getLogger(MessageUserService.class);
	private final  SessionService sessionService;
	private final  MessageUserRepository messageUserRepository;
	private final PasswordEncoder passwordEncoder;
	
	public MessageUserService(SessionService sessionService, MessageUserRepository messageUserRepository, PasswordEncoder passwordEncoder) {
		this.sessionService = sessionService;
		this.messageUserRepository = messageUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@Transactional
	public UserId addUser(String username, String password) {
		MessageUserEntity messageUserEntity = messageUserRepository.save(
				new MessageUserEntity(
						username,
						passwordEncoder.encode(password)
				)
		);
		log.info("User {} added, userId : {}", username, messageUserEntity.getUserId());
		return new UserId(messageUserEntity.getUserId());
	}
	
	@Transactional
	public void removeUser() {
		String username = sessionService.getUsername();
		messageUserRepository.findByUsername(username).ifPresent(messageUserEntity -> {
			log.info("User {} removed", username);
			messageUserRepository.delete(messageUserEntity);
		});
	}
	
}
