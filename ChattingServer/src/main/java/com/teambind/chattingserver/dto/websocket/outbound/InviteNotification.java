package com.teambind.chattingserver.dto.websocket.outbound;

import com.teambind.constant.MessageType;
import lombok.Getter;

@Getter
public class InviteNotification extends BaseMessage {

	private final String username;
	
	public InviteNotification(String username) {
		super(MessageType.ASK_INVITE);
		this.username = username;
		
	}
}
