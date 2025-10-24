package com.teambind.messagesystem.service;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.file.Paths;

public class TerminalService {
	private Terminal terminal;
	private LineReader lineReader;
	
	private TerminalService() {
	}
	
	public static TerminalService create() throws IOException {
		TerminalService terminalService = new TerminalService();
		
		try {
			terminalService.terminal = TerminalBuilder.builder().system(true).build();
		} catch (IOException e) {
			System.err.println("Failed to create TerminalService. error : " + e.getMessage());
			throw e;
		}
		terminalService.lineReader = LineReaderBuilder.builder().terminal(terminalService.terminal).variable(LineReader.HISTORY_FILE, Paths.get("./data/history.txt")).build();
		return terminalService;
	}
	
	public String readLine(String prompt) {
		String input = lineReader.readLine(prompt);
		terminal.puts(InfoCmp.Capability.cursor_up);
		terminal.puts(InfoCmp.Capability.delete_line);
		terminal.flush();
		return input;
	}
	
	public void PrintMessage(String username, String content) {
		lineReader.printAbove(String.format("%s : %s", username, content));
	}
	
	public void printSystemMessage(String content) {
		lineReader.printAbove(" ==> " + content);
	}
	
	public void clearTerminal() {
		terminal.puts(InfoCmp.Capability.clear_screen);
		terminal.flush();
	}
}
