package com.teambind.auth.auth;

import com.teambind.auth.entity.UserEntity;
import com.teambind.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	private final UserRepository userRepository;
	Logger log = LoggerFactory.getLogger(UserDetailsService.class);
	
	public UserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> {
			log.info("User not found: {}", username);
			return new UsernameNotFoundException("User not found");
		});
		return new MessageUserDetails(
				userEntity.getUserId(),
				userEntity.getUsername(),
				userEntity.getPassword()
		);
	}
	
	;
	
	
}
