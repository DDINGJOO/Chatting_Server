package com.teambind.chattingserver.dto.websocket.inbound;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.teambind.constant.MessageType;
import lombok.Getter;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = WriteMessageRequest.class, name = MessageType.WRITE_MESSAGE),
		@JsonSubTypes.Type(value = KeepAliveRequest.class, name = MessageType.KEEP_ALIVE),
		@JsonSubTypes.Type(value = InviteRequest.class, name=  MessageType.INVITE_REQUEST)
})
@Getter
public abstract class BaseRequest {
	private final String type;
	
	public BaseRequest(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}

