package com.teambind.chattingserver.dto.websocket.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teambind.constant.MessageType;


public class MessageRequest extends BaseRequest{
	
	private final String username;
	private final String content;
	
	@JsonCreator
	public MessageRequest(
			@JsonProperty("username") String username,
			@JsonProperty("content") String content){
		super(MessageType.WRITE_MESSAGE);
		this.username = username;
		this.content = content;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getContent() {
		return content;
	}
}
