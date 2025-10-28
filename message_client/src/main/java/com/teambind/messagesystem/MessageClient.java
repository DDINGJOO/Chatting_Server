package com.teambind.messagesystem;


import com.teambind.messagesystem.dto.websocket.outbound.MessageRequest;
import com.teambind.messagesystem.handler.CommandHandler;
import com.teambind.messagesystem.handler.WebSocketMessageHandler;
import com.teambind.messagesystem.handler.WebSocketSender;
import com.teambind.messagesystem.service.RestApiService;
import com.teambind.messagesystem.service.TerminalService;
import com.teambind.messagesystem.service.WebSocketService;
import com.teambind.messagesystem.util.JsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;

public class MessageClient {
	
	
	public static void main(String[] args) {
		final String BASE_URL = "localhost:8080";
		final String WEBSOCKET_PATH = "/ws/v1/message";
		TerminalService terminalService;
		try {
			terminalService = TerminalService.create();
		} catch (IOException e) {
			System.err.println("Failed to create TerminalService. error : " + e.getMessage());
			return;
		}

		JsonUtil.setTerminalService(terminalService);
		RestApiService restApiService = new RestApiService(terminalService, BASE_URL);
		WebSocketSender webSocketSender = new WebSocketSender(terminalService);
		WebSocketService webSocketService = new WebSocketService(terminalService, webSocketSender, BASE_URL, WEBSOCKET_PATH);
		webSocketService.setWebSocketMessageHandler(new WebSocketMessageHandler(terminalService));
		CommandHandler commandHandler = new CommandHandler(restApiService, webSocketService, terminalService);
		
		while (true) {
			
			String input = terminalService.readLine("Enter message : ");
			
			if (!input.isEmpty() && input.charAt(0) == '/') {
				String[] parts = input.split(" ", 2);
				String command = parts[0].substring(1); // Remove leading '/'
				String arguments = parts.length > 1 ? parts[1] : "";
				
				if (!commandHandler.process(command, arguments)) {
					break;
				}
			} else if (!input.isEmpty()) {
				terminalService.PrintMessage("me", input);
				webSocketService.sendMessage(new MessageRequest("test client", input));
				
			}
		}
	}
}
