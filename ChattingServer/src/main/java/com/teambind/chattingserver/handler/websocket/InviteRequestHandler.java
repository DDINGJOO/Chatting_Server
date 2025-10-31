package com.teambind.chattingserver.handler.websocket;

import com.teambind.auth.entity.UserId;
import com.teambind.chattingserver.dto.websocket.inbound.InviteRequest;
import com.teambind.chattingserver.dto.websocket.outbound.ErrorResponse;
import com.teambind.chattingserver.dto.websocket.outbound.InviteNotification;
import com.teambind.chattingserver.dto.websocket.outbound.InviteResponse;
import com.teambind.chattingserver.service.UserConnectionService;
import com.teambind.chattingserver.session.WebSocketSessionManager;
import com.teambind.constant.Constants;
import com.teambind.constant.MessageType;
import com.teambind.constant.UserConnectionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Component
public class InviteRequestHandler implements BaseRequestHandler<InviteRequest> {
	
	private final UserConnectionService userConnectionService;
	private final WebSocketSessionManager sessionManager;
	
	public InviteRequestHandler(UserConnectionService userConnectionService, WebSocketSessionManager sessionManager) {
		this.userConnectionService = userConnectionService;
		this.sessionManager = sessionManager;
	}
	
	@Override
	public void handleRequest(WebSocketSession senderSession, InviteRequest request) {
		UserId inviterUserId = (UserId)senderSession.getAttributes().get(Constants.USER_ID.getValue());
		Pair<Optional<UserId>, String> result = userConnectionService.invite(inviterUserId, request.getUserInviteCode());
		result.getFirst().ifPresentOrElse(partinerUserId -> {
			String inviterUsername = result.getSecond();
			sessionManager.sendMessage(senderSession, new InviteResponse(request.getUserInviteCode(),
					UserConnectionStatus.PENDING));
			
			sessionManager.sendMessage(
					sessionManager.getSession(partinerUserId), new InviteNotification(inviterUsername));
		},
				() -> {
			String errorMessage = result.getSecond();
			sessionManager.sendMessage(senderSession, new ErrorResponse(MessageType.INVITE_REQUEST, errorMessage));
		});
	}
}
