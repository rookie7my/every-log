# EveryLog - 블로그 서비스

개발자들을 위한, Markdown 을 지원하는 Simple 한 블로그 서비스

## How to run
```
# build
./gradlew build

# run
java -jar build/libs/everylog-0.0.1-SNAPSHOT.jar
```
- 기본적으로 `h2 database`를 `in-memory mode`로 사용합니다.
- 다른 데이터베이스를 사용하려면, `application.properties` 의 `datasource` 관련 설정을 수정해주세요.

## Tech Stack
- Java, JavaScript
- Spring Boot, Spring Web MVC, Spring Data JPA, Spring Security
- JPA/Hibernate, h2 database, MariaDB
- Thymeleaf
- Gradle
- Lombok
- WebJars, Bootstrap