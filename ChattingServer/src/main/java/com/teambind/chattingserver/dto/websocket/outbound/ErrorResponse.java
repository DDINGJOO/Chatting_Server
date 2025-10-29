package com.teambind.chattingserver.dto.websocket.outbound;

import com.teambind.constant.MessageType;
import lombok.Getter;

@Getter
public class ErrorResponse extends BaseMessage {

	private final String messageType;
	private final String message;
	
	public ErrorResponse(String messageType, String message) {
		super(MessageType.ERROR);
		this.messageType = messageType;
		this.message = message;
	}
}
