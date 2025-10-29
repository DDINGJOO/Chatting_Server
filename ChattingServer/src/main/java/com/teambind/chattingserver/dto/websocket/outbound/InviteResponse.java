package com.teambind.chattingserver.dto.websocket.outbound;

import com.teambind.auth.dto.InviteCode;
import com.teambind.constant.MessageType;
import com.teambind.constant.UserConnectionStatus;
import lombok.Getter;

@Getter
public class InviteResponse extends BaseMessage{
	
	private final InviteCode inviteCode;
	private final UserConnectionStatus status;
	
	
	public InviteResponse(InviteCode inviteCode, UserConnectionStatus status) {
		super(MessageType.INVITE_RESPONSE);
		this.inviteCode = inviteCode;
		this.status = status;
	}
}
