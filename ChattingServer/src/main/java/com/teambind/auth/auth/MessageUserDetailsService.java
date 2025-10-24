package com.teambind.auth.auth;

import com.teambind.auth.entity.MessageUserEntity;
import com.teambind.auth.repository.MessageUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageUserDetailsService implements UserDetailsService {
	
	private final MessageUserRepository messageUserRepository;
	Logger log = LoggerFactory.getLogger(MessageUserDetailsService.class);
	
	public MessageUserDetailsService(MessageUserRepository messageUserRepository) {
		this.messageUserRepository = messageUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MessageUserEntity messageUserEntity = messageUserRepository.findByUsername(username).orElseThrow(() -> {
			log.info("User not found: {}", username);
			return new UsernameNotFoundException("User not found");
		});
		return new MessageUserDetails(
				messageUserEntity.getUserId(),
				messageUserEntity.getUsername(),
				messageUserEntity.getPassword()
		);
	}
	
	;
	
	
}
