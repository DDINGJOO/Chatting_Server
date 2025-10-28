package com.teambind.messagesystem.service;

import com.teambind.messagesystem.dto.websocket.outbound.BaseRequest;
import com.teambind.messagesystem.dto.websocket.outbound.KeepAliveRequest;
import com.teambind.messagesystem.dto.websocket.outbound.MessageRequest;
import com.teambind.messagesystem.handler.WebSocketMessageHandler;
import com.teambind.messagesystem.handler.WebSocketSender;
import com.teambind.messagesystem.handler.WebSocketSessionHandler;
import com.teambind.messagesystem.util.JsonUtil;
import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketService {
	
	private final TerminalService terminalService;
	private final WebSocketSender webSocketSender;
	private final String webSocketUrl;
	private WebSocketMessageHandler webSocketMessageHandler;
	private Session session;
	private ScheduledExecutorService scheduledExecutorService = null;
	
	public WebSocketService(TerminalService terminalService, WebSocketSender webSocketSender, String url, String endpoint) {
		this.terminalService = terminalService;
		this.webSocketSender = webSocketSender;
		this.webSocketUrl = "ws://" + url + endpoint;
	}
	
	public void setWebSocketMessageHandler(WebSocketMessageHandler webSocketMessageHandler) {
		this.webSocketMessageHandler = webSocketMessageHandler;
	}
	
	public boolean createSession(String sessionId) {
		ClientManager clientManager = ClientManager.createClient();
		
		ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
			@Override
			public void beforeRequest(Map<String, List<String>> headers) {
				headers.put("Cookie", List.of("SESSION=" + sessionId));
			}
		};
		
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();
		try {
			session = clientManager.connectToServer(new WebSocketSessionHandler(terminalService, this), config,
					new URI(webSocketUrl));
			session.addMessageHandler(webSocketMessageHandler);
			enableKeepAlive();
			return true;
		} catch (Exception e) {
			terminalService.printSystemMessage(String.format("Failed to connect to [%s] error: [%s]", webSocketUrl, e.getMessage()));
			return false;
		}
	}
	
	public void closeSession() {
		if (session != null) {
			try {
				if (session.isOpen()) {
					session.close(
							new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "NORMAL_CLOSURE")
					);
				}
				session = null;
			} catch (Exception e) {
				terminalService.printSystemMessage(String.format("Failed to close session. error: [%s]", e.getMessage()));
			}
		}
	}
	
	
	public void sendMessage(BaseRequest baseRequest) {
		
		if (session != null && session.isOpen()) {
			if(baseRequest instanceof MessageRequest messageRequest)
			{
				webSocketSender.sendMessage(session, messageRequest);
				return;
			}
			JsonUtil.toJson(baseRequest)
					.ifPresent(payload ->
							session
									.getAsyncRemote()
									.sendText(
											payload,
											result -> {
												if(!result.isOK())
												{
													terminalService.printSystemMessage(
															" '%s' send failed. cause: %s".formatted(payload, result.getException().getMessage()));
												}
											}));
		} else {
			terminalService.printSystemMessage("Session is not open. Message not sent.");
		}
	}
	
	private void enableKeepAlive(){
		if(scheduledExecutorService == null){
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		}
		scheduledExecutorService.scheduleAtFixedRate( ()
				-> sendMessage(new KeepAliveRequest()) , 1,1, TimeUnit.MINUTES);
	}
	
	private void disableKeepAlive(){
		if(scheduledExecutorService != null){
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}
}
