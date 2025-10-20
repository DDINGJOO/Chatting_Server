package com.teambind.chattingserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teambind.chattingserver.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {
	private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private WebSocketSession leftside = null;
	private WebSocketSession rightside = null;
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("Connection Closed: {} from{}",status, session.getId());
		if (leftside == session) {
			leftside = null;
		}
		else if( rightside == session){
			rightside = null;
		}
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("Message Received: {} from {}", message.getPayload(), session.getId());
		String payload = message.getPayload();
		try{
			Message receivedMessage = objectMapper.readValue(payload, Message.class);
			if(leftside == session)
			{
				// todo : rightside == null ?
				sendMessage(rightside, receivedMessage.content());
			}
			else if(rightside == session)
			{
				sendMessage(leftside, receivedMessage.content());
			}
			else
			{
				log.warn("not exist session : {}", session.getId());
			}
		}catch (Exception e)
		{
			String errorMsg = "유효한 프로토콜이 아닙니다. : " + e.getMessage();
			log.error("erroeMessage payload : {}, from {}", payload, session.getId());
			sendMessage(session, errorMsg);
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("TransPortError: {} , from : {}", exception.getMessage(), session.getId());
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("Connection Established: {}", session.getId());
		
		if (leftside == null) {
			leftside = session;
			return;
		}
		else if( rightside == null){
			rightside = session;
			return;
		}
		
		log.warn("not exist empty session : {}", session.getId());
		session.close();
	}

	
	private void sendMessage(WebSocketSession session, String message)  {
		try{
			String msg = objectMapper.writeValueAsString(new Message(message));
			session.sendMessage(new TextMessage(msg));
			log.info("sendMessage : {} to {}", msg, session.getId());
		}
		catch (Exception e)
		{
			log.error("메세지 발송 실패 to {} error : {} ",session.getId(), e.getMessage());
			
		}
	}
}
