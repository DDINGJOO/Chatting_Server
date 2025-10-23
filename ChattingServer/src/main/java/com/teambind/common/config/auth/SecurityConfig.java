package com.teambind.common.config.auth;

import com.teambind.auth.auth.RestApiLoginAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@SuppressWarnings("unused")
public class SecurityConfig {
	Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	;
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder()
				.username("testuser")
				.password(passwordEncoder().encode("testpass"))
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(userDetails);
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService detailsService, PasswordEncoder encoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(detailsService);
		provider.setPasswordEncoder(encoder);
		return new ProviderManager(provider);
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity httpSecurity,
			AuthenticationManager authenticationManager
	) throws Exception {
		RestApiLoginAuthFilter post = new RestApiLoginAuthFilter(new AntPathRequestMatcher("/api/v1/auth/login", "ws/v1/connect"), authenticationManager);
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.addFilterAt(post, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/login")
						.permitAll()
						.anyRequest().authenticated())
				.logout(logout -> logout.logoutUrl("/api/v1/auth/logout").logoutSuccessHandler(this::logoutHandler));
		
		return httpSecurity.build();
	}
	
	private void logoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		response.setCharacterEncoding("UTF-8");
		String message;
		
		if (authentication != null && authentication.isAuthenticated()) {
			response.setStatus(HttpStatus.OK.value());
			message = "로그아웃 성공";
		} else {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			message = "로그아웃 실패";
		}
		try {
			response.getWriter().write(message);
		} catch (Exception e) {
			log.error("logoutHandler error : {}", e.getMessage());
		}
		
	}
}
