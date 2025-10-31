package com.teambind.chattingserver.handler;

import com.teambind.chattingserver.dto.websocket.inbound.BaseRequest;
import com.teambind.chattingserver.handler.websocket.BaseRequestHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@SuppressWarnings("rawtypes,unchecked")
public class RequestHandlerDispatcher {
	
	private final Map<Class<? extends BaseRequest>, BaseRequestHandler<? extends BaseRequest>> handlerMap = new HashMap<>();
	private final ListableBeanFactory beanFactory;
	
	public RequestHandlerDispatcher(ListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	@PostConstruct
	public void prepareRequestHandlerMapping() {
		Map<String, BaseRequestHandler> requestHandlerMap = beanFactory.getBeansOfType(BaseRequestHandler.class);
		for (BaseRequestHandler requestHandler : requestHandlerMap.values()) {
			Class<? extends BaseRequest> requestType = getRequestType(requestHandler);
			if (requestType != null) {
				handlerMap.put(requestType, requestHandler);
			} else {
				log.error("Request Handler Mapping Error : {}", requestHandler.getClass().getName());
			}
		}
	}
	
	private Class<? extends BaseRequest> getRequestType(BaseRequestHandler request) {
		for (Type type : request.getClass().getGenericInterfaces()) {
			if (type instanceof ParameterizedType parameterizedType &&
					parameterizedType.getRawType().equals(BaseRequestHandler.class)) {
				return (Class<? extends BaseRequest>) parameterizedType.getActualTypeArguments()[0];
			}
		}
		return null;
	}
}
