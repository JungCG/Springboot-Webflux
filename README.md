# Springboot-Webflux
1. **Basic knowledge of WebFlux**
2. **Project using Spring boot, WebFlux**

--------------------------------------------

## Contents
1. [Using](#using)
2. [Reactive Programming](#reactive-programming)
3. [WebFlux와 SSE](#webFlux와-sse)
4. [Projects](#projects)
5. [License](#license)

--------------------------------------------

## Using
1. Spring Reactive Web : Servlet으로 동작 안하고 Netty라는 서버 사용 / 비동기 서버로 동작
2. Spring Data R2DBC [참고](https://spring.io/guides/gs/accessing-data-r2dbc/) : 비동기 데이터베이스 지원 / JPA를 사용하면 Blocking 발생 (단일 스레드 의미가 없다.)

--------------------------------------------

## Reactive Programming
1. 정의 - (Reactive, 반응형) 어떤 요청이 있을 때 **기다리지 않고 응답**한다.
2. 예시
    1. **Reactive가 아닌 경우 1**
        1. 클라이언트가 서버에게 요청
        2. 서버가 DB에 요청
        3. DB가 데이터를 확인하고 서버에게 응답
        4. 서버가 클라이언트에게 응답
    2. **Reactive가 아닌 경우 2**
        1. A가 요청을 안하면 B는 아무것도 하지 않는다.
        2. A가 요청을 해야 B가 응답
3. **Reactive가 아닌 경우 문제점**
    1. **대기 시간이 생긴다.**
        - 클라이언트는 자바스크립트를 이용해서 비동기 처리하면 되기 때문에 문제가 되는 곳은 서버
        - 클라이언트가 요청했을 때 서버가 DB에 요청하는 데 DB가 응답을 해줄때까지 다른 클라이언트의 요청을 받지 못하는 게 문제.
        - 기존에는 스레드를 늘리는 방법으로 해결했다.
        - 이 경우 Time Slicing을 하면서 컨텍스트 스위칭 때문에 느려지는 문제가 발생한다.
    2. **클라이언트가 요청을 할 때만 서버가 응답한다.**
4. **해결 방법**
    1. 다른 일을 할 수 있게 하거나, 기다리는 시간을 줄인다. -> **이벤트 루프**
    2. 요청이 없어도 응답 => 스크림 유지 (WebSocket) -> 응답만 유지 (**SSE 프로토콜**, Server Send Event)
    3. **정리**
        1. 서버가 데이터베이스에 요청
        2. 데이터베이스가 응답을 다이렉트로 서버에 알려줌. ("10초 걸린다." 메시지)
        3. 서버는 응답을 다이렉트로 클라이언트에게 알려줌. ("10초 걸린다." 메시지)
        4. 서버는 할일 없으니 다른 요청을 처리할 수 있음
        5. 10초 뒤 클라이언트에게 요청을 다시 해줘야 되기 때문에 기억하고 있어야 한다. : 처리하지 못한 이벤트에 대한 기억이 필요 (이벤트 루프에 클라이언트의 요청을 기억)
        6. 다른 처리를 하고 있다가 중간중간 이벤트 루프를 확인 -> 클라이언트의 요청이 종료가 되었으면 클라이언트에게 응답해줌
        7. http 통신은 Stateless 이기 때문에 "10초 걸린다" 라는 응답을 하고는 연결이 끊어진다. 그래서 연결을 안 끊고 유지시킨다. (WebSocket) -> 요청은 필요없고 응답만 유지하면 된다. (SSE 프로토콜)

--------------------------------------------

## WebFlux와 SSE
1. **SSE** (Server Send Event)
    - **서버쪽에서 주도적으로 메시지를 send 할 수 있는 이벤트**
    - **Push 기술**
    - **지속적인 응답을 할 수 있는 Stream** (= Flux, 흐름)
2. **WebFlux**
    - Flux + Spring = WebFlux
    - 개념
        1. 구독 Subscribe - **Response 선이 유지**되고 있다. (Flux)
        2. 출판 Publishing - **Reponse 선으로 지속적으로 데이터를 응답**해준다.
3. 추가 개념
    - RDBMS는 "10초 걸린다" 라는 메시지를 보낼 수 없다.
    - MongoDB 같은 NoSQL을 사용해야 한다.
    - R2DBC에서는 RDBMS에서 비동기 처리 가능 (비동기 데이터베이스)
    - Flux는 지속적으로 응답 (1개 이상), Mono는 한번만 응답 (0~1개)
4. **SSE와 WebFlux 비교**
    - WebFlux
        1. 단일스레드, 비동기 + Stream
        2. 백프레셔가 적용된 데이터만큼 간헐적 응답이 가능
        3. 데이터 소비가 끝나면 응답이 종료
    - SSE
        1. Servlet, WebFlux
        2. 데이터 소비가 끝나도 Stream 계속 유지

--------------------------------------------

## Projects
1. Project **reactive**-**test** : webflux library를 사용하지 않고 구현 (원리)
    1. **MyPub**.java : Publisher 구현
    2. **MySub**.java : Subscriber 구현
    3. **MySubscription**.java : Subscription 구현
2. Project **fluxtest** : 필터를 이용하여 Flux 구현
    1. **MyFilter**.java : Flux 구현
    2. **MyFilter2**.java : Data 추가
    3. **MyFilterConfig**.java : MyFilter, MyFilter2 필터 등록
    4. **EventNotify**.java : 입력 데이터, 상태 클래스
3. Project **flux** : Reactive Streams 사용하여 구현
    1. **Customer**.java : Customer 클래스
    2. **CustomerRepository**.java : extends ReactiveCrudRepository<Customer, Long> (JPA가 아니다.)
    3. **CustomerController**.java : Controller 클래스
    4. **DBInit**.java [참고](https://spring.io/guides/gs/accessing-data-r2dbc/) : DB 초기화 및 테스트
    5. schema.sql : Customer 테이블 생성
    
--------------------------------------------

## License
- **Source Code** based on [codingspecialist](https://github.com/codingspecialist)