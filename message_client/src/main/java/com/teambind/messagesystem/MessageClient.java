package com.teambind.messagesystem;

import com.teambind.messagesystem.service.TerminalService;

import java.io.IOException;

public class MessageClient {
	public static void main(String[] args) {
		TerminalService terminalService;
		try{
			terminalService = TerminalService.create();
		} catch (IOException e) {
			System.err.println("Failed to create TerminalService. error : " + e.getMessage());
			return;
		}
		
		
		while (true) {
			
			String input = terminalService.readLine("Enter message : ");
			
			if (!input.isEmpty() && input.charAt(0) == '/') {
				String command = input.substring(1);
				if(command.equals("exit")) {
					break;
				}
				if(command.equals("clear")) {
					terminalService.clearTerminal();
				}
				else if(!input.isEmpty())
				{
					terminalService.printSystemMessage(command);
					continue;
				}
				
			}
			terminalService.PrintMessage("you", input);
		}
	}
}
