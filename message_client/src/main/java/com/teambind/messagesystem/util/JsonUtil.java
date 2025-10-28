package com.teambind.messagesystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teambind.messagesystem.service.TerminalService;

import java.util.Optional;

public class JsonUtil {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static TerminalService terminalService;
	
	public static void setTerminalService(TerminalService terminalService) {
		JsonUtil.terminalService = terminalService;
	}
	
	public static <T> Optional<T> fromJson(String json, Class<T> clazz) {
		try {
			return Optional.of(objectMapper.readValue(json, clazz));
		} catch (Exception e) {
			if (terminalService != null) {
				terminalService.printSystemMessage("Failed to convert JSON to object: " + e.getMessage());
			}
			return Optional.empty();
		}
	}
	
	public static Optional<String> toJson(Object object) {
		try {
			return Optional.of(objectMapper.writeValueAsString(object));
		} catch (Exception e) {
			if (terminalService != null) {
				terminalService.printSystemMessage("Failed to convert object to JSON: " + e.getMessage());
			}
			return Optional.empty();
		}
	}
	
	
}
