# Lincall-backend
2022-2 capstone design 'Lincall'

### DB setting
* client table

  CREATE TABLE IF NOT EXISTS client (
  id VARCHAR(10) NOT NULL,
  password VARCHAR(10) NOT NULL,
  email VARCHAR(20) NOT NULL,
  name VARCHAR(10) NOT NULL,
  PRIMARY KEY(id),
  CHECK (email LIKE '%@%')
  );


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

### File Directory
* 상담사 프로필 사진 저장 : ../image/profile/{userID}.png


### API
| Method | URI                   | Description         | input                                                                                 | output          |
|--------|-----------------------|---------------------|---------------------------------------------------------------------------------------|-----------------|
|POST| user/client/signUp    | client 정보 db에 저장    | {"id" : "user1", "password" : "1234", "email" : "sejong@sju.ac.kr", "name" : "user1"} | boolean         |
|POST| user/client/logIn     | client 로그인 처리       | {"id" : "user1", "password" : "1234"}                                                 | boolean         |
|GET| user/client/id-check  | client id 중복 체크 | ?id = { client id }                                                                   | boolean         | 
|POST| user/rooms-test       | 현재 join 가능한 상담 방 목록 | none                                                                                  | List\<String\>  |
|GET| user/email-auth       | 입력한 이메일로 인증 키 전송 | ?email = {사용자 email 주소}                                                               | String (인증키)    |
|POST| user/profile          | 상담사 프로필 사진 저장 | form-data (userID, image)                                                             | none            |
|POST| AI/flask-test         | flask server 통신 테스트 | none                                                                                  | "Hello World!"  |
|POST| AI/question           | 유사 질문 추천 | String (질문)                                                                           | String (유사한 질문) |



### WebSocket 
* WebSocket 접속 : ws://localhost:8080/signal
* message 순서 : client [join] -> counselor [join] -> counselor [offer] -> client [answer] -> counselor [ice]] -> client [ice]
