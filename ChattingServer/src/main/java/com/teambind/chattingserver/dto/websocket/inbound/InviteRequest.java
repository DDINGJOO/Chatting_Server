package com.teambind.chattingserver.dto.websocket.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teambind.auth.dto.InviteCode;
import com.teambind.constant.MessageType;
import lombok.Getter;



public class InviteRequest extends BaseRequest{
	
	private final InviteCode userInviteCode;


	
	@JsonCreator
	public InviteRequest(
			@JsonProperty("userInviteCode") InviteCode userInviteCode){
		super(MessageType.INVITE_REQUEST);
		this.userInviteCode = userInviteCode;
		
	}
	
	public InviteCode getUserInviteCode() {
		return userInviteCode;
	}
}
