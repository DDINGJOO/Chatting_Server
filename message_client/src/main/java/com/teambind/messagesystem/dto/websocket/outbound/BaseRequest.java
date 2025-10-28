package com.teambind.messagesystem.dto.websocket.outbound;


public abstract class BaseRequest {
	private final String type;
	
	public BaseRequest(String type) {
		this.type = type;
	}
	
}

