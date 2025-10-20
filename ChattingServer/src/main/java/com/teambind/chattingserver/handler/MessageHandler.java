package com.teambind.chattingserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teambind.chattingserver.dto.Message;
import com.teambind.chattingserver.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final WebSocketSessionManager sessionManager;
	
	public MessageHandler(WebSocketSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
		log.info("Connection Closed: {} from{}", status, session.getId());
		sessionManager.terminateSession(session.getId());
		log.warn("not exist empty session : {}", session.getId());
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession senderSession, TextMessage message) {
		log.info("Message Received: {} from {}", message.getPayload(), senderSession.getId());
		String payload = message.getPayload();
		try {
			Message receivedMessage = objectMapper.readValue(payload, Message.class);
			sessionManager.getSessions().forEach(
					participantSession -> {
						if (!participantSession.equals(senderSession)) {
							sendMessage(participantSession, receivedMessage);
						}
					}
			);
		} catch (Exception e) {
			String errorMsg = "유효한 프로토콜이 아닙니다. : " + e.getMessage();
			log.error("erroeMessage payload : {}, from {}", payload, senderSession.getId());
			sendMessage(senderSession, new Message("system", errorMsg));
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		log.error("TransPortError: {} , from : {}", exception.getMessage(), session.getId());
		sessionManager.terminateSession(session.getId());
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("Connection Established: {}", session.getId());
		//TODO : overflowStrategy And Param
		ConcurrentWebSocketSessionDecorator concurrentWebSocketSessionDecorator =
				new ConcurrentWebSocketSessionDecorator(session, 5000, 100 * 1024);
		
		sessionManager.storeSession(concurrentWebSocketSessionDecorator);
	}
	
	
	private void sendMessage(WebSocketSession session, Message message) {
		try {
			String msg = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(msg));
			log.info("sendMessage : {} to {}", msg, session.getId());
		} catch (Exception e) {
			log.error("메세지 발송 실패 to {} error : {} ", session.getId(), e.getMessage());
			
		}
	}
}
