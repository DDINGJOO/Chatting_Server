package com.teambind.chattingserver.controller;

import com.teambind.auth.dto.UserRegisterRequest;
import com.teambind.chattingserver.service.MessageUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/auth")
public class MessageUserController {
	Logger log = LoggerFactory.getLogger(MessageUserController.class);
	private final MessageUserService messageUserService;
	
	public MessageUserController(MessageUserService messageUserService) {
		this.messageUserService = messageUserService;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRegisterRequest req) {
		try {
			var response = messageUserService.addUser(req.username(), req.password());
			return ResponseEntity.ok("User registered successfully: ");
		}catch (
				Exception e
		){
			log.error("register error : {}", e.getMessage());
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	@PostMapping("/unregister")
	public ResponseEntity<String> unregister(HttpServletRequest request) {
		try {
			messageUserService.removeUser();
			request.getSession().invalidate();
			return ResponseEntity.ok("User unregistered successfully");
		}catch (
				Exception e
		){
			log.error("unregister error : {}", e.getMessage());
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
}
