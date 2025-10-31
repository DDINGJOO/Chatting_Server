# RequestHandlerDispatcher 아키텍처 분석

## 개요

`RequestHandlerDispatcher`는 WebSocket 요청을 적절한 핸들러로 자동으로 라우팅하는 타입 안전한 디스패처 패턴 구현체입니다. Java의 Reflection과 Generic을 활용하여 **컴파일 타임 타입 안전성**과 **런타임 동적 핸들러 매핑**을 동시에 달성한 우아한 아키텍처입니다.

## 핵심 설계 철학

### 1. 제네릭 기반 타입 안전성
```java
public <T extends BaseRequest> void dispatch(WebSocketSession webSocketSession, T request)
```
- 각 요청 타입에 대해 타입 안전한 핸들러 매칭을 보장
- 컴파일러가 타입 불일치를 사전에 감지

### 2. 리플렉션 기반 자동 매핑
```java
@PostConstruct
public void prepareRequestHandlerMapping()
```
- 애플리케이션 시작 시 모든 핸들러를 자동으로 스캔하고 등록
- 새로운 핸들러 추가 시 별도의 설정 파일 수정 불필요

### 3. 전략 패턴 + 팩토리 패턴 하이브리드
- Map 기반 핸들러 저장소로 O(1) 시간 복잡도의 디스패칭 달성
- Spring의 `ListableBeanFactory`를 활용한 IoC 컨테이너 통합

---

## 상세 구현 분석

### 1. 핸들러 맵 구조
```java
private final Map<Class<? extends BaseRequest>, BaseRequestHandler<? extends BaseRequest>> handlerMap = new HashMap<>();
```

**설계 포인트:**
- Key: 요청 클래스 타입 (`Class<? extends BaseRequest>`)
- Value: 해당 요청을 처리하는 핸들러 인스턴스
- 런타임에 요청 객체의 실제 클래스를 키로 사용하여 핸들러 조회

**장점:**
- 요청 타입과 핸들러의 1:1 매핑 보장
- 빠른 조회 성능 (HashMap의 O(1) 시간 복잡도)

---

### 2. 디스패치 메서드

```java
public <T extends BaseRequest> void dispatch(WebSocketSession webSocketSession, T request) {
    BaseRequestHandler<T> handler = (BaseRequestHandler<T>) handlerMap.get(request.getClass());
    if(handler != null) {
        handler.handleRequest(webSocketSession, request);
        return;
    }
    log.error("No handler found for request type : {}", request.getClass().getSimpleName());
}
```

**실행 흐름:**

1. **타입 파라미터 추론**: `T extends BaseRequest`로 요청 타입 제한
2. **핸들러 조회**: `request.getClass()`로 런타임 타입을 키로 사용
3. **안전한 캐스팅**: `@SuppressWarnings("rawtypes,unchecked")`로 제네릭 타입 소거 처리
4. **핸들러 실행**: 찾은 핸들러에 요청 위임
5. **예외 처리**: 핸들러 미발견 시 에러 로깅

**타입 안전성:**
- 컴파일 타임: `T extends BaseRequest` 제약으로 잘못된 타입 방지
- 런타임: `handlerMap`에서 조회한 핸들러가 해당 타입 처리 보장

---

### 3. 핸들러 자동 등록 시스템

```java
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
```

**실행 시점:**
- `@PostConstruct` 애노테이션으로 Spring Bean 초기화 직후 실행
- 애플리케이션 시작 시 한 번만 실행되어 성능 오버헤드 최소화

**동작 과정:**

1. **핸들러 스캔**: `ListableBeanFactory`로 모든 `BaseRequestHandler` 빈 조회
2. **타입 추출**: 각 핸들러가 처리하는 요청 타입을 리플렉션으로 추출
3. **맵 등록**: `handlerMap`에 (요청타입, 핸들러) 쌍 저장
4. **검증**: 타입 추출 실패 시 에러 로깅

**확장성:**
- 새로운 핸들러를 Spring Bean으로 등록하기만 하면 자동으로 디스패처에 등록
- 설정 파일이나 매핑 코드 수정 불필요 (Open-Closed Principle)

---

### 4. 타입 추출의 마법 (핵심 로직)

```java
// TODO : study deep dive
private Class<? extends BaseRequest> getRequestType(BaseRequestHandler request) {
    for (Type type : request.getClass().getGenericInterfaces()) {
        if (type instanceof ParameterizedType parameterizedType &&
                parameterizedType.getRawType().equals(BaseRequestHandler.class)) {
            return (Class<? extends BaseRequest>) parameterizedType.getActualTypeArguments()[0];
        }
    }
    return null;
}
```

**이 메서드가 하는 일:**
핸들러 클래스가 구현한 인터페이스의 제네릭 타입 파라미터를 런타임에 추출

**예시로 이해하기:**

만약 다음과 같은 핸들러가 있다면:
```java
public class ChatMessageHandler implements BaseRequestHandler<ChatMessageRequest> {
    @Override
    public void handleRequest(WebSocketSession session, ChatMessageRequest request) {
        // 처리 로직
    }
}
```

**단계별 실행:**

1. **`request.getClass().getGenericInterfaces()`**
   - 반환: `[BaseRequestHandler<ChatMessageRequest>]`
   - 핸들러가 구현한 모든 인터페이스의 제네릭 정보 조회

2. **`type instanceof ParameterizedType`**
   - `BaseRequestHandler<ChatMessageRequest>`가 파라미터화된 타입인지 확인
   - 제네릭 타입 정보가 포함되어 있는지 체크

3. **`parameterizedType.getRawType().equals(BaseRequestHandler.class)`**
   - Raw Type이 `BaseRequestHandler`인지 확인
   - 다른 인터페이스(예: `Serializable`)는 필터링

4. **`parameterizedType.getActualTypeArguments()[0]`**
   - `<ChatMessageRequest>` 부분 추출
   - 첫 번째 타입 인자가 핸들러가 처리하는 요청 타입

5. **결과:**
   - 반환값: `ChatMessageRequest.class`
   - 이 값이 `handlerMap`의 키로 사용됨

**왜 이게 필요한가?**

Java의 제네릭은 **타입 소거(Type Erasure)** 때문에 런타임에 `T`의 실제 타입을 알 수 없습니다.
```java
// 컴파일 후에는 이렇게 됨
BaseRequestHandler handler = new ChatMessageHandler();
// T가 ChatMessageRequest라는 정보는 사라짐!
```

하지만 **인터페이스에 선언된 제네릭 타입 정보는 클래스 메타데이터에 남아있습니다.**
이를 `ParameterizedType`으로 접근하여 런타임에 타입 정보를 복원하는 것입니다.

**리플렉션 API 계층 구조:**
```
Type (최상위 인터페이스)
├── Class
├── ParameterizedType  ← 제네릭 타입 (예: List<String>)
├── TypeVariable       ← 타입 변수 (예: T)
├── GenericArrayType   ← 제네릭 배열 (예: T[])
└── WildcardType       ← 와일드카드 (예: ? extends Number)
```

---

## 사용 예시

### 1. 핸들러 정의

```java
@Component
public class ChatMessageHandler implements BaseRequestHandler<ChatMessageRequest> {
    @Override
    public void handleRequest(WebSocketSession session, ChatMessageRequest request) {
        // 채팅 메시지 처리 로직
    }
}

@Component
public class InviteUserHandler implements BaseRequestHandler<InviteUserRequest> {
    @Override
    public void handleRequest(WebSocketSession session, InviteUserRequest request) {
        // 사용자 초대 처리 로직
    }
}
```

### 2. WebSocket 메시지 수신 시

```java
@Override
public void handleTextMessage(WebSocketSession session, TextMessage message) {
    BaseRequest request = parseMessage(message); // JSON → 객체 변환

    // 자동으로 적절한 핸들러로 디스패치
    dispatcher.dispatch(session, request);
}
```

### 3. 실행 흐름

```
1. 클라이언트가 ChatMessageRequest 전송
   ↓
2. parseMessage()가 JSON을 ChatMessageRequest 객체로 변환
   ↓
3. dispatcher.dispatch(session, chatMessageRequest)
   ↓
4. handlerMap.get(ChatMessageRequest.class)
   → ChatMessageHandler 반환
   ↓
5. ChatMessageHandler.handleRequest() 실행
```

---

## 아키텍처의 장점

### 1. 타입 안전성
- 컴파일 타임에 핸들러-요청 타입 불일치 감지
- 런타임 ClassCastException 위험 최소화

### 2. 확장성 (Open-Closed Principle)
- 새 핸들러 추가 시 기존 코드 수정 불필요
- `@Component`만 붙이면 자동 등록

### 3. 성능
- HashMap 기반 O(1) 핸들러 조회
- 애플리케이션 시작 시 한 번만 리플렉션 사용

### 4. 가독성
- 각 핸들러가 독립적인 클래스로 분리
- 책임이 명확하게 분리됨 (Single Responsibility Principle)

### 5. 테스트 용이성
- 각 핸들러를 독립적으로 단위 테스트 가능
- 디스패처 로직과 핸들러 로직 분리

---

## 개선 가능한 부분

### 1. 핸들러 중복 등록 방지
```java
if (handlerMap.containsKey(requestType)) {
    throw new IllegalStateException(
        "Duplicate handler for " + requestType.getSimpleName()
    );
}
handlerMap.put(requestType, requestHandler);
```

### 2. 슈퍼클래스 핸들러 지원
현재는 정확히 일치하는 타입만 처리합니다. 상속 관계를 고려한 조회도 가능합니다:
```java
private BaseRequestHandler<?> findHandler(Class<?> requestClass) {
    BaseRequestHandler<?> handler = handlerMap.get(requestClass);
    if (handler != null) return handler;

    // 부모 클래스 핸들러 찾기
    for (Class<?> superClass : getAllSuperclasses(requestClass)) {
        handler = handlerMap.get(superClass);
        if (handler != null) return handler;
    }
    return null;
}
```

### 3. 핸들러 체인 패턴
여러 핸들러가 순차적으로 처리하도록 확장:
```java
private final Map<Class<?>, List<BaseRequestHandler<?>>> handlerChainMap;
```

---

## 관련 디자인 패턴

1. **전략 패턴 (Strategy Pattern)**
   - 각 핸들러가 교체 가능한 전략
   - 디스패처가 런타임에 전략 선택

2. **팩토리 패턴 (Factory Pattern)**
   - `handlerMap`이 핸들러 팩토리 역할
   - 요청 타입에 따라 적절한 핸들러 생성/반환

3. **커맨드 패턴 (Command Pattern)**
   - 각 요청이 캡슐화된 커맨드 객체
   - 핸들러가 커맨드 실행자

4. **싱글톤 패턴 (Singleton Pattern)**
   - Spring의 `@Component`로 싱글톤 보장
   - 핸들러 맵을 애플리케이션 전역에서 공유

---

## 결론

`RequestHandlerDispatcher`는 Java의 고급 기능을 활용하여 확장 가능하고 타입 안전한 요청 처리 시스템을 구현한 훌륭한 예시입니다. 특히 리플렉션을 통한 제네릭 타입 추출 기법은 Spring Framework의 내부 구현에서도 자주 사용되는 패턴으로, 이를 잘 이해하면 프레임워크 레벨의 설계 능력을 기를 수 있습니다.

**핵심 교훈:**
- 컴파일 타임 안전성과 런타임 유연성은 양립할 수 있다
- 리플렉션은 비용이 크지만, 초기화 시점에만 사용하면 성능 문제 없다
- 좋은 아키텍처는 새로운 기능 추가 시 기존 코드 수정을 최소화한다

---

## 참고 자료

- [Java Reflection API Documentation](https://docs.oracle.com/javase/tutorial/reflect/)
- [ParameterizedType JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/ParameterizedType.html)
- [Spring ListableBeanFactory](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/ListableBeanFactory.html)
- Effective Java 3rd Edition - Item 33: Consider typesafe heterogeneous containers