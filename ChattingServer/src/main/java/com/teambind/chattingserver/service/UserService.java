package com.teambind.chattingserver.service;


import com.teambind.auth.entity.UserEntity;
import com.teambind.auth.entity.UserId;
import com.teambind.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	private final Logger log = LoggerFactory.getLogger(UserService.class);
	private final  SessionService sessionService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(SessionService sessionService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.sessionService = sessionService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@Transactional
	public UserId addUser(String username, String password) {
		UserEntity userEntity = userRepository.save(
				new UserEntity(
						username,
						passwordEncoder.encode(password)
				)
		);
		log.info("User {} added, userId : {}", username, userEntity.getUserId());
		return new UserId(userEntity.getUserId());
	}
	
	@Transactional
	public void removeUser() {
		String username = sessionService.getUsername();
		userRepository.findByUsername(username).ifPresent(userEntity -> {
			log.info("User {} removed", username);
			userRepository.delete(userEntity);
		});
	}
	
}
