package com.teambind.messagesystem.service;

import com.teambind.messagesystem.handler.WebSocketMessageHandler;
import com.teambind.messagesystem.handler.WebSocketSender;
import com.teambind.messagesystem.handler.WebSocketSessionHandler;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketService {
	
	private final TerminalService terminalService;
	private final WebSocketSender webSocketSender;
	private final String webSocketUrl;
	private WebSocketMessageHandler webSocketMessageHandler;
	private Session session;
	
	public WebSocketService(TerminalService terminalService, WebSocketSender webSocketSender, String url, String endpoint) {
		this.terminalService = terminalService;
		this.webSocketSender = webSocketSender;
		this.webSocketUrl = "ws://" + url + endpoint;
	}
	
	public void setWebSocketMessageHandler(WebSocketMessageHandler webSocketMessageHandler) {
		this.webSocketMessageHandler = webSocketMessageHandler;
	}
	
	public boolean createSession() throws URISyntaxException {
		ClientManager clientManager = ClientManager.createClient();
		try {
			session = clientManager.connectToServer(new WebSocketSessionHandler(terminalService), new URI(webSocketUrl));
			session.addMessageHandler(webSocketMessageHandler);
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
			} catch (Exception e) {
				terminalService.printSystemMessage(String.format("Failed to close session. error: [%s]", e.getMessage()));
			}
		}
	}
}
