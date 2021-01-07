### 스프링 부트 자동-구성(autoconfiguration) 의 특징

- 스프링 어플리케이션 개발과정을 단순화
- xml구성으로 명시적으로 빈을 구성하지 않고, 구성속성 (configuration property)를 사용해 여러 원천 속성중에서 설정 가능
  - JVM 시스템 속성
  - 명령형 인자
  - 환경변수 등

### 자동-구성 세부조정

- 스프링의 2가지 구성
  - 빈 연결
    - 스프링 애플리케이션 컨텍스트에서 빈으로 생성되는 애플리케이션 컴포넌트 및 상호간에 주입되는 방법을 선언하는 구성
  - 속성 주입
    - 빈의 속성값을 설정하는 구성
- 스프링부트가 H2의존성 라이브러리를 classpath에서 찾아 사용할 수 있다면 schema.sql 과 data.sql을 자동으로 찾아서 데이터베이스에 적용시킨다
- 만약 sql 이름을 다르게 만들고 싶거나, 3가지 이상의 sql 파일을 적용하고 싶을 시 구성속성을 사용할 수 있다!
- asdasf

### 스프링 환경 추상화

- 스프링 환경 추상화에서는 다음과 같은 속성의 근원으로부터 원천 속성을 가져온다
  - 원천속성
    - JVM 시스템 속성
    - 운영체제의 환경변수
    - 명령행 인자
    - 애플리케이션 속성 구성파일 (application.properties, application.yml)
- 스프링 환경 추상화에서는 이 속성들을 한군데로 모은 후 각 속성이 주입되는 스프링 빈을 사용할 수 있게 해준다.
  - 스프링 컨텍스트 각종 빈
    - 데이터 소스
    - 사용자 서비스
    - 제품 서비스 등
- server.port 설정예제

### 구성-요소 사용하기

- 구성속성을 사용하는 3가지 방법

    ```jsx
    1. sever.port: 9090 (application.properties)
    2. server:
    		 port: 9090 (application.yml)
    3. $java -jar tacocloud-0.0.5-SNAPSHOT.jar --server.port: 9090
    4. $export SERVER_PORT: 9090 (spring boot가 server.port로 변환해서 읽는다)
    ```

- 데이터 소스 구성하기
  - 3가지 이상의 sql 파일을 사용하고 싶을 시 아래와 같이 설정해주면 된다.

      ```jsx
      schema:
            - order-schema.sql
            - ingredient-schema.sql
            - taco-schema.sql
            - user-schema.sql
          data:
            - ingredients.sql
      ```

  - JNDI(Java Naming and Directory Interface API) 을 사용한 설정

    ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8d520716-037e-4894-a7f7-55c86d10f222/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8d520716-037e-4894-a7f7-55c86d10f222/Untitled.png)

  - mysql을 사용한 기본적인 databse 구성

      ```jsx
      spring:
        jpa:
          generate-ddl: true
          show-sql: true
          properties:
            hibernate:
              format_sql: true
        datasource:
          url: jdbc:mysql://localhost:3306/tacocloud
          username: tacodb
          password: tacepassword
              driver-class-name: com.mysql.jdbc.Driver
      ```

- 내장 서버 구성하기
  - https 접근을 위한 key 생성

    ```jsx
    keytool -keystore mykey.jks -genkey -alias tomcat -keyalg RSA
    ```

- 서버 구성 설정

    ```jsx
    server:
      port: 8443
      ssl:
        key-store: file:///etc/keys/mykeys.jks
        key-store-password: letmein
        key-password: letmein
    ```

- 로깅 구성
  - classpath 의 루트에 logback.xml 파일을 통해 설정 가능
  - 스프링 부트에서는 구성요소를 사용하여 설정이 가능하다

    ```jsx
    logging:
      file: #책에 나온 방식은 deprecated 됨
        path: /var/logs
        name: TacoCloud.log
      level:
        root: warn
        org.springframework.jdbc.core.JdbcTemplate: debug
        org.hibernate.type: trace
        org.springframework.security: debug
    ```

- 다른 속성의 값을 가져와서 사용하는것도 가능하다

    ```jsx
    greeting:
    	welcome: ${spring.application.name}
    ```

### 우리의 구성속성 생성 및 사용하기

```jsx
taco:
  orders:
    pageSize: 10
```

```jsx
@ConfigurationProperties(prefix="taco.orders")
```

### 프로파일 사용해서 구성하기

- 프로파일 구성하기
  - 두가지 이상의 다른 환경에서 설치 및 배포를 할 때 사용된다
  - 3개의 하이픈 (`—--`) 을 사용하여 구분해준다

    ```jsx
    logging:
    	level: 
    		tacos: DEBUG
    ---
    spring:
    	profiles: prod
    	datasource: 
    		url: jdbc:mysql://localhost/tacocloud
    		username: tacouser
    		password: tacopassword
    logging:
    	level:
    		tacos:WARN

    ---
    spring 
    	profiles: qa

    ```

  - application-{stage}.yml 또는 properties 파일로 여러개의 프로파일 구성요소를 제어 가능하다.
- 프로파일 활성화하기

    ```yaml
    spring:
    	profiles:
    		active: dev
    ```

    ```jsx
    $export SPRING_PROFILES_ACTIVE=prod
    ```

    ```jsx
    $java -jar tacocloud-0.0.5-SNAPSHOT.jar --spring.profiles.active=prod
    ```

  - 여러가지 프로파일을 한번에 활성화 하는것도 가능 (아직 어떻게 작동하는지 테스트는 못해봤습니다 ㅠ.ㅠ 다음주 시작할때 공유하도록 하겠습니다)

      ```yaml
      spring:
          profiles:
              active: 
                  - prod
                  - audit
                  - ha
      ```

  - 활성화 된 프로파일에 따라 조건별로 빈 생성하기

      ```java
      @Bean
      @Profile("dev") OR @Profile({"dev", "qa"} 
      OR @Profile("!prod") OR @Profile({"!prod", "!qa"})
      ```