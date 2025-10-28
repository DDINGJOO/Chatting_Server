package com.teambind.messagesystem.dto.websocket.outbound;

import com.teambind.messagesystem.contents.MessageType;


public class KeepAliveRequest extends BaseRequest{
	public KeepAliveRequest() {
		super(MessageType.KEEP_ALIVE);
	}
}
