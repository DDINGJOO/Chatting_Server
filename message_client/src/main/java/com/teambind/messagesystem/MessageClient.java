package com.teambind.messagesystem;

import com.teambind.messagesystem.dto.Message;
import com.teambind.messagesystem.handler.WebSocketMessageHandler;
import com.teambind.messagesystem.handler.WebSocketSender;
import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.service.WebSocketService;

import java.io.IOException;
import java.net.URISyntaxException;

public class MessageClient {
	
	
	public static void main(String[] args) throws URISyntaxException {
		final String WEBSOCKET_BASE_URL = "localhost:8080";
		final String WEBSOCKET_PATH = "/ws/v1/message";
		TerminalService terminalService;
		try {
			terminalService = TerminalService.create();
		} catch (IOException e) {
			System.err.println("Failed to create TerminalService. error : " + e.getMessage());
			return;
		}
		
		
		WebSocketSender webSocketSender = new WebSocketSender(terminalService);
		WebSocketService webSocketService = new WebSocketService(terminalService, webSocketSender, WEBSOCKET_BASE_URL, WEBSOCKET_PATH);
		webSocketService.setWebSocketMessageHandler(new WebSocketMessageHandler(terminalService));
		
		
		while (true) {
			
			String input = terminalService.readLine("Enter message : ");
			
			if (!input.isEmpty() && input.charAt(0) == '/') {
				String command = input.substring(1);
				
				
				boolean exit = switch (command) {
					case "exit" -> {
						webSocketService.closeSession();
						yield true;
					}
					
					case "clear" -> {
						terminalService.clearTerminal();
						yield false;
					}
					case "connect" -> {
						webSocketService.createSession();
						yield false;
					}
					default -> false;
				};
				
				if(exit)
					break;
			} else if (!input.isEmpty()) {
				terminalService.PrintMessage("me", input);
				webSocketService.sendMessage(new Message("test client", input));
				
			}
		}
	}
}
