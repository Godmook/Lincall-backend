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
### API
| Method | URI                | Description      | input                                                                                | output          |
|--------|--------------------|------------------|--------------------------------------------------------------------------------------|-----------------|
|POST| user/client/signup | client 정보 db에 저장 | {"id" : "user1", "password" : "1234", "email" : "sejong@sju.ac.kr", "name" : "user1"} | boolean         |
|POST| user/client/logIn  | client 로그인 처리| {"id" : "user1", "password" : "1234"}                                                | boolean         | 
|POST| user/flask-test | (http://localhost:5000) 통신 테스트 | String| "hello world!"  |
|POST| user/rooms-test| 현재 join 가능한 상담 방 목록 | | List\<String\> |


### WebSocket 
* WebSocket 접속 : ws://localhost:8080/signal
* message 순서 : client [join] -> counselor [join] -> counselor [offer] -> client [answer] -> counselor [ice]] -> client [ice]
