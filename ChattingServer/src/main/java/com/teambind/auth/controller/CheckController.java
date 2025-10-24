package com.teambind.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CheckController {
	@GetMapping("/check")
	public ResponseEntity<String> check() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			return ResponseEntity.ok("인증된 사용자" + auth.getName());
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자");
		}
	}
}
