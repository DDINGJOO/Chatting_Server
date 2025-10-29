package com.teambind.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonUtil {
	
	private final ObjectMapper objectMapper;
	
	
	
	public  <T> Optional<T> fromJson(String json, Class<T> clazz) {
		try {
			return Optional.of(objectMapper.readValue(json, clazz));
		} catch (Exception e) {
			log.error("Failed to convert JSON to object: {}", e.getMessage());
			return Optional.empty();
		}
	}
	
	public  Optional<String> toJson(Object object) {
		try {
			return Optional.of(objectMapper.writeValueAsString(object));
		} catch (Exception e) {
			log.error("Failed to convert object to JSON: {}", e.getMessage());
			return Optional.empty();
		}
	}
	
	
}
