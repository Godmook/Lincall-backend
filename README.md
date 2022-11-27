# Lincall-backend
2022-2 capstone design 'Lincall'

### DB setting
* database create
  create database lincall character set "utf8" collate 'utf8_icelandic_ci';
  use lincall;

* client table

  CREATE TABLE IF NOT EXISTS client (
  id VARCHAR(10) NOT NULL,
  password VARCHAR(10) NOT NULL,
  email VARCHAR(20) NOT NULL,
  name VARCHAR(10) NOT NULL,
  PRIMARY KEY(id),
  CHECK (email LIKE '%@%')
  );


* counselor table

  CREATE TABLE IF NOT EXISTS counselor (
  id VARCHAR(10) NOT NULL,
  password VARCHAR(10) NOT NULL,
  email VARCHAR(20) NOT NULL,
  name VARCHAR(10) NOT NULL,
  PRIMARY KEY(id),
  CHECK (email LIKE '%@%')
  );


* consulting

  CREATE TABLE IF NOT EXISTS consulting (
  id INT AUTO_INCREMENT,
  counselor VARCHAR(10),
  client VARCHAR(10),
  start long,
  end long,
  PRIMARY KEY(id)
  );
 
 
* Message

  CREATE TABLE IF NOT EXISTS Message(
  id INT AUTO_INCREMENT,
  roomId INT NOT NULL,
  type VARCHAR(20),
  time long,
  emotion VARCHAR(20),
  text VARCHAR(100),
  keyword VARCHAR(100),
  PRIMARY KEY(id)
  );


* test case

  delete from client;
  
  delete from counselor;
  
  delete from consulting;
  
  delete from message;
  
  alter table consulting auto_increment = 1;
  
  alter table message auto_increment = 1;
  
  insert into client values("client1", "1111", "client1@naver.com", "정현진"), ("client2", "2222", "client2@naver.com", "김예진");
  
  insert into counselor values("counselor1", "1111", "counselor1@naver.com", "오창묵"), ("counselor2", "2222", "counselor2@naver.com", "윤석진");
  
  insert into consulting values("1", "counselor1", "client1", 1668847708518, 1668848008518, null), ("2", "counselor1", "client2", 1668847908518, 1668848208518, null), ("3", "counselor2", "client2", 1668847708518, 1668848908518, null) ;
  
  insert into message values("1", "1",  "client", 1669302000000, "angry", "주문하고 한시간 지났는데 안와요.", "주문"), ("2", "1", "counselor", 1669602000000, "none", "배달 출발했습니다.", null), ("3", "1", "notice", 1669702000000, "none", "지금부터 고객 음성이 차단됩니다.", null);

### properties
* application-mariaDB.properties

```properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3306/{DB_NAME}
spring.datasource.username={DB_USERNAME}
spring.datasource.password={DB_PASSWORD}
```

* application-email.properties

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username={ID}
spring.mail.password={PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
 [gmail 앱 비밀번호 설정](https://support.google.com/mail/answer/185833?hl=ko)

* vito.properties

```properties
ClientID = {client id}
ClientSecret = {client secret}
```

* flaskServer.properties

```properties
flaskURL = {flask server ip address}
```



### File Directory

* 상담사 프로필 사진 저장 : ../image/profile/{userID}.png
* 고객 음성 저장 파일 : ../voice/room0_client.wav
* 상담사 음성 저장 파일 : ../voice/room0_counselor.wav


### API
* UserController

| Method | URI                   | Description         | input                                                                                               | output         |
|--------|-----------------------|---------------------|-----------------------------------------------------------------------------------------------------|----------------|
| POST   | user/client/signUp    | user 정보 db에 저장    | {"id" : "client1", "password" : "1234", "email" : "client1@sju.ac.kr", "name" : "client1"}          | boolean        |
| POST   | user/client/logIn       | user 로그인 처리       | {"id" : "client1", "password" : "1234"}                                                             | User           |
| GET    | user/client/id-check    | user id 중복 체크 | ?id = { user id }                                                                                   | boolean        | 
| POST   | user/counselor/signUp | counselor 정보 db에 저장    | {"id" : "counselor1", "password" : "1234", "email" : "counselor1@sju.ac.kr", "name" : "counselor1"} | boolean        |
| POST   | user/counselor/logIn       | counselor 로그인 처리       | {"id" : "counselor1", "password" : "1234"}                                                          | User           |
| GET    | user/counselor/id-check    | counselor id 중복 체크 | ?id = { user id }                                                                                   | boolean        |
| POST   | user/counselor/updatePassword| counselor password 변경| {"id" : "counselor1", "curPassword" : "1234", "newPassword" : "0000"}                               | String (결과 안내)|
|GET| user/counselor/withdrawal| counselor 탈퇴 | ?id={counselor id} | none| 
| GET    | user/email-auth       | 입력한 이메일로 인증 키 전송 | ?email = {사용자 email 주소}                                                                             | String (인증키)   |
| POST   | user/profile          | 상담사 프로필 사진 저장 | form-data (userID, image)                                                                           | none           |


* AIController

| Method | URI                   | Description         | input                                                                                      | output          |
|--------|-----------------------|---------------------|--------------------------------------------------------------------------------------------|-----------------|
|POST| AI/flask-test         | flask server 통신 테스트 | none                                                                                       | "Hello World!"  |
|POST| AI/question           | 유사 질문 추천 | String (질문)                                                                                | String (유사한 질문) |


* ConsultingController

| Method | URI                             | Description | input                  | output                               |
|--------|---------------------------------|------------|------------------------|--------------------------------------|
| GET    | consulting/create               | 새로운 상담 생성  | none                   | int (consulting id)                  |
| GET    | consulting/end                  | 상담 종료 시간 업데이트 | ?id={consultingID}     | none                                 |
| GET    | consulting/records/client       | 고객 상담 기록   | ?clientID = {clientID} | List\<ConsultingView\>               |
| GET    | consulting/records/counselor    | 고객 상담 기록   | ?id = {counselorID}    | List\<ConsultingView\>               |
|GET| consulting/room-list            | 현재 상담방 리스트 | none                   | List\<Room\>                         |
|GET| consulting/counselorInfo        | 상담사 상담 정보(이번달, 오늘 총 상담 시간) | ?id={counselorID}      | String({"month" : 00, "today" : 00}) |
|GET| consulting//counselorInfo/today | 상담사 상담 정보(오늘 상담 건수, 오늘 상담 시간) | ?id={counselorID}      | String({"count":12,"time":3960000})  |
|GET| consulting/waitClient           | 현재 대기 중인 고객 수 | none                   | int                                  |


* MainController

| Method | URI                     | Description              | input                                       | output             |
|--------|-------------------------|--------------------------|---------------------------------------------|--------------------|
|POST| main/addText            | 상담 중 대화 저장               | {"roomId" : 1,"from" : "client" or "counselor", "time" : 1669302000,"encodeStr" : "encode .wav"} | none               |
|GET| main/dialogue           | 상담 대화 기록 확인              | ?roomId= {consulting id}                    | List\<Message\>    |
|GET| main/angerPoint         | 고객이 화내기 시작한 부분의 대화 기록 확인 | ?roomId= {consulting id}                    | List\<AngerPoint\> |
|GET| main/todayKeyword/happy | 오늘의 긍정 키워드 목록            | none                                        | List\<String\>     |
|GET| main/todayKeyword/angry | 오늘의 부정 키워드 목록            | none                                        | List\<String\>     |


### WebSocket 

  websocket request URL  : ws://localhost:8080/ws
  
  Subscription URL : /sub/room/{room id}
  
  WebSocket Message 형식 : {"type" : "offer", "sender" : "user1", "channelId" : 1 , "data" : "data..."}

* WebSocketMessageController

| Destination Queue | Description | Message                                               | send             |
|-------------------|-------------|-------------------------------------------------------|------------------|
| /pub/join         | 상담방 입장      | WebsocketMessage (type = "client" or "counselor")     | "{sender} join"  |
| /pub/data         | 데이터 전송      | WebsocketMessage (type = "offer" or "answer" or "ice") | WebsocketMessage |
| /pub/sucess       | 상담 시작       | WebsocketMessage (type = "client" or "counselor") | WebsocketMessage |
| /pub/quit         | 상담방 나가기     | WebsocketMessage (type = "client" or "counselor") | "{sender} quit"  |


* Websocket message

  - "reload anger starting point" : 고객이 화를 내기 시작한 경우 반환.
  
  - "activate voice blocking" : 고객이 화를 4번 낸 경우 음성 차단 기능 활성화 알림.



* 통신 순서
  1. http://localhost:8080/consulting/create로 상담방 생성 요청 (client)
  2. client - websocket 연결 + /sub/room/{roomID} 구독
  3. client - /pub/join 발행
  4. counselor - websocket 연결 + /sub/room/{roomID} 구독
  5. counselor - /pub/join 발행
  6. client - /pub/data로 offer 발행
  7. counselor - /pub/data로 answer 발행
  8. client - /pub/data로 ice 발행
  9. counselor - /pub/data로 ice 발행
  10. client/counselor - /pub/sucess 발행