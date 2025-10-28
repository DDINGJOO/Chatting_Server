package com.teambind.chattingserver.dto.websocket.inbound;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.teambind.contents.MessageType;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = MessageRequest.class, name = MessageType.MESSAGE),
		@JsonSubTypes.Type(value = MessageRequest.class, name = MessageType.KEEP_ALIVE),
	
})
@Getter
public abstract class BaseRequest {
	private final String type;
	
	public BaseRequest(String type) {
		this.type = type;
	}
}

