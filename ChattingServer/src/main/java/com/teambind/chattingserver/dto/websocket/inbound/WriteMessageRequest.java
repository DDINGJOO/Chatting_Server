package com.teambind.chattingserver.dto.websocket.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teambind.constant.MessageType;
import lombok.Getter;



public class WriteMessageRequest extends BaseRequest{
	public String getUsername() {
		return username;
	}
	
	private final String username;
	private final String content;
	
	@JsonCreator
	public WriteMessageRequest(
			@JsonProperty("username") String username,
			@JsonProperty("content") String content){
		super(MessageType.WRITE_MESSAGE);
		this.username = username;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
}
