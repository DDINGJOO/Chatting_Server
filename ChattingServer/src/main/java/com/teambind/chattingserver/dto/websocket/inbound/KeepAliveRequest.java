package com.teambind.chattingserver.dto.websocket.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.teambind.contents.MessageType;



public class KeepAliveRequest extends BaseRequest{
	@JsonCreator
	public KeepAliveRequest() {
		super(MessageType.KEEP_ALIVE);
	}
}
