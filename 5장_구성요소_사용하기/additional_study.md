## Types of logging in SpringBoot 2

slf4j

- Logger 추상체(wrapper)로 slf4j는 logback이나 log4j2와 같은 프레임워크의 인터페이스의 역할을 해주기 때문에 로깅 구현체가 바뀌더라도 사용이 가능하다

### 구현체

- log4j
    - Apache의 기본적인 로깅프레임워크로 2015년부터 지원이 중단됨.
    - 로그레벨
        - FATAL: 아주 심각한 에러가 발생한 상태
        - ERROR: 어떠한 요청을 처리하는 중 문제가 발생한 상태
        - WARN: 실행에는 문제가 ㅇ벗지만 향후 시스템 에러의 원인이 될 수 있는 경고성 메세지
        - INFO: 상태변경이나 정보성 메세지
        - DEBUG: 개발시 디버그 용도로 사용하는 메세지
        - TRACE: 디버그 레벨이 너무 광범위함을 해결하기 위해 좀 더 상세한 이벤트를 나타냄
    - 구성요소
        - Logger: 출력할 메시지를 Appender에 전달
        - Appender: 전달된 로그를 어디에 출력할지 결정.
        - Layout: 로그를 어떤 형식으로 출력할지 결정.
    - 설정파일: log4j.xml

        ```yaml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE log4j:configuration PUBLIC "-//APACHE//DTD LOG4J 1.2//EN" "log4j.dtd">
        <log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
         
            <!-- Appenders -->
            <appender name="console" class="org.apache.log4j.ConsoleAppender">
                <param name="Target" value="System.out" />
                <layout class="org.apache.log4j.PatternLayout">
                    <param name="ConversionPattern" value="[%d{yyyy-MM-dd HH:mm:ss}] %-5p: %c - %m%n" />
                </layout>
            </appender>
            
            <logger name="freemarker">
            	<level value="off"/>
            </logger>
            
            <!-- Application Loggers -->
            <logger name="com.joycity.template">
                <level value="debug" />
            </logger>
            
            <!-- 3rdparty Loggers -->
            <logger name="org.springframework.core">
                <level value="info" />
            </logger>    
            
            <logger name="org.springframework.beans">
                <level value="info" />
            </logger>
            
            <logger name="org.springframework.context">
                <level value="info" />
            </logger>
         
            <logger name="org.springframework.web">
                <level value="info" />
            </logger>
         
            <!-- Root Logger -->
            <root>
                <priority value="debug" />
                <appender-ref ref="console" />
            </root>
            
        </log4j:configuration>
        ```

- logback
    - log4j에 비해 향상된 성능과 필터링 옵션을 제공하며 slf4j를 지원한다
    - 자동리로드 기능 지원
        - 서버를 재가동 할 필요 없이 logback.xml의 변경사항 자동 리로드 가능
    - 설정파일 : logback.xml

        ```yaml
        <?xml version="1.0" encoding="UTF-8"?>
        <configuration>
            <!-- Console Log Setting -->
            <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
                <layout class="ch.qos.logback.classic.PatternLayout">
                    <encoder>
        <!--                 <pattern>[%d{yyyy/MM/dd HH:mm:ss}] %-5level %cyan(%-25.25logger{0}) | %msg%n</pattern> -->
                    	<Pattern>[%d{yyyy-MM-dd HH:mm:ss}] %-4level [%cyan(%logger{36})] - %msg%n</Pattern>
                    </encoder>
                </layout>
            </appender>
         
         	<logger name="com.joycity.test">
        		<level value="off" />
        	</logger>
        	
        	<!-- 3rdparty Loggers -->
        	<logger name="org.springframework.core">
        		<level value="INFO" />
        	</logger>
        	
        	<logger name="org.springframework.beans">
        		<level value="INFO" />
        	</logger>
        	
        	<logger name="org.springframework.context">
        		<level value="INFO" />
        	</logger>

        	<logger name="org.springframework.web">
        		<level value="INFO" />
        	</logger>
         
            <root level="INFO">
                <appender-ref ref="STDOUT" />
            </root>
         
        </configuration>
        ```

- log4j2
    - logback과 같이 자동리로드 및 필터링 옵션을 지원한다
    - 멀티쓰레드를 사용할 시 log4j에 비해 월등한 성능을 보여준다 (async 로깅시 약 18배)
        - ![성능](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl64lP%2FbtqIAPPq9Ih%2Fhp0QkqBnH034BBJ7RmrFv0%2Fimg.png)
        - [https://junshock5.tistory.com/124](https://junshock5.tistory.com/124) #성능테스트 관련
    - 설정파일 : log4j2.xml

        ```yaml
        <?xml version="1.0" encoding="UTF-8"?>
        <Configuration status="warn" >
            <Appenders>
                <Console name="STDOUT" target="SYSTEM_OUT">
                    <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
                </Console>
            </Appenders>
            <Loggers>
                <Root level="info">
                    <AppenderRef ref="STDOUT"/>
                </Root>
            </Loggers>
        </Configuration>
        ```

### Logging Dependencies

- logging dependencies는 spring-boot-starter-* 라이브러리에 공통적으로 포함되어있다.

  ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/02661738-3ad9-41c4-893a-d8b4871d0f0c/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/02661738-3ad9-41c4-893a-d8b4871d0f0c/Untitled.png)

- spring-boot-starter-logging 내부에 logback, log4j2, jul 3가지 라이브러리가 들어있다
- springboot는 기본적으로 logback을 사용하여 로깅한다.
- slf4j로 log4j2를 사용하려면 logback depencency를 제외시키고 log4j2.xml / yml 파일로 설정을 해주어야 함.