package com.teambind.messagesystem.service;


import com.teambind.messagesystem.dto.LoginRequest;
import com.teambind.messagesystem.dto.UserRegisterRequest;
import com.teambind.messagesystem.util.JsonUtil;
import org.glassfish.grizzly.http.util.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class RestApiService {
	
	private final TerminalService terminalService;
	private final String url;
	private String sessionId;
	
	public RestApiService(TerminalService terminalService, String url) {
		this.terminalService = terminalService;
		this.url = "http://" + url;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public boolean register(String username, String password) {
		return request("/api/v1/auth/register", "", new UserRegisterRequest(username, password)).map(
				httpResponse
						-> httpResponse.statusCode() == HttpStatus.OK_200.getStatusCode()).orElse(false);
	}
	
	public boolean unregister() {
		if (sessionId.isEmpty()) {
			return false;
		}
		return request("/api/v1/auth/unregister", sessionId, null).map(
				httpResponse
						-> httpResponse.statusCode() == HttpStatus.OK_200.getStatusCode()).orElse(false);
	}
	
	public boolean login(String username, String password)
	{
		return request("/api/v1/auth/login", "", new LoginRequest(username, password)).map(
				httpResponse
						->{
					if (httpResponse.statusCode() == HttpStatus.OK_200.getStatusCode()) {
						sessionId = httpResponse.body();
						return true;
					} else {
						return false;}})
				.orElse(false);
	}
	
	public boolean logout() {
		if (sessionId.isEmpty()) {
			return false;
		}
		return request("/api/v1/auth/logout", sessionId, null).filter(
				httpResponse
						-> httpResponse.statusCode() == HttpStatus.OK_200.getStatusCode())
				.isPresent();
	}
	
	private Optional<HttpResponse<String>> request(String path, String sessionId, Object requestObject) {
		try {
			HttpRequest.Builder builder = HttpRequest.newBuilder()
					.uri(new URI(url + path))
					.header("Content-Type", "application/json");
			if (!sessionId.isEmpty()) {
				builder.header("Cookie", "SESSION=" + sessionId);
			}
			if (requestObject != null) {
				JsonUtil.toJson(requestObject).ifPresent(
						jsonBody -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
				);
			} else {
				builder.POST(HttpRequest.BodyPublishers.noBody());
			}
			
			HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
			terminalService.printSystemMessage("Response Status : %d, Body : %s ".formatted(httpResponse.statusCode(), httpResponse.body()));
			return Optional.of(httpResponse);
		} catch (Exception e) {
			terminalService.printSystemMessage("API call failed. cause : %s)".formatted(e.getMessage()));
			return Optional.empty();
		}
	}
}
