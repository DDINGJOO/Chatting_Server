package com.teambind.chattingserver.dto.websocket.outbound;

public abstract class BaseMessage {
	
	private String type;
	public BaseMessage(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	
}
