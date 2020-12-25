# Chapter 1. 스프링 시작하기

## 01. 스프링이란?

---

- 스프링 애플리케이션 컨텍스트(Spring application context)
    - 컨테이너(container)
    - 애플리케이션 컴포넌트(빈)들을 생성하고 관리한다.

- 빈(bean)
    - 스프링 애플리케이션 컨텍스트 내부에서 서로 연결되어 완전한 애플리케이션을 만든다.

- 의존성 주입(Dependency Injection)
    - 빈의 상호 연결 패턴
    - 애플리케이션 컴포넌트에서 의존하는 다른 빈의 생성과 관리를 컨테이너가 해주며, 이 개체에서 모든 컴포넌트를 생성, 관리하고 해당 컴포넌트를 필요로 하는 빈에 주입한다.
    - 생성자 인자 또는 속성의 접근자 메서드를 통해 처리된다.
    - 최신 버전 스프링에서는 XML 방식 대신 자바 기반의 구성이 더 많이 사용된다.

        ```xml
        <bean id="inventoryService" class="com.example.InventoryService" />

        <bean id="productService" class="com.example.ProductService">
            <constructor-args ref="inventoryService" />
        </bean>
        ```

        ```java
        @Configuration
        public class ServiceConfiguration {
            @Bean
            public InventoryService inventoryService() {
                return new InventoryService();
            }

            @Bean
            public ProductService productService() {
                return new ProductService(inventoryService());
            }
        }
        ```

    - @Configuration
        - 각 빈을 스프링 애플리케이션 컨텍스트에 제공하는 구성 클래스라는 것을 스프링에게 알려준다.
        - 구성 클래스의 메서드에는 @Bean 이 지정되어 있고, 이것은 각 메서드의 반환 객체가 애플리케이션 컨텍스트의 빈으로 추가되어야 한다는 것을 나타낸다.
        - 기본적으로 각 빈의 ID가 해당 빈을 정의하는 메서드 이름과 동일하다.

    - 자동-구성
        - 자동 연결과 컴포넌트 탐색 스프링 기법을 기반으로 한다.
        - 컴포넌트 탐색(component scanning)
            - 스프링은 자동으로 애플리케이션의 classpath에 지정된 컴포넌트를 찾은 후 스프링 애플리케이션 컨텍스트의 빈으로 생성할 수 있다.
        - 자동 연결(autowiring)
            - 의존 관계가 있는 컴포넌트를 자동으로 다른 빈에 주입한다.

    - 스프링 부트(Spring boot)
        - 생산성 향상을 제공하는 스프링 프레임워크의 확장
        - 향상된 자동-구성(autoconfiguration) 기능에 의해 환경 변수인 classpath를 기준으로 어떤 컴포넌트가 구성되고 연결되어야 하는지 알 수 있다.

## 02. 스프링 애플리케이션 초기 설정하기

---

- 스프링 Initializr
    - REST API를 사용하는 브러우저 기반의 웹 어플리케이션
    - 원하는 기능을 구현할 수 있는 스프링 프로젝트의 구조를 생성해준다.

- 스프링 프로젝트 구조
    - src/main/java
        - 애플리케이션 소스 코드
    - src/test/java
        - 테스트 코드
    - src/main/resources
        - 자바 리소스가 아닌 것
    - mvnw와 mvnw.cmd / gradlew와 gradle.bat
        - 메이븐, 그래들 래퍼 스크립트. 메이븐, 그래들이 컴퓨터에 설치되있지 않더라도 이 스크립트를 사용하여 프로젝트를 빌드할 수 있다.
    - pom.xml / build.gradle
        - 메이븐/그래들 빌드 명세(빌드할 때 필요한 정보)를 지정한 파일
    - xxApplication.java
        - 스프링 부트 메인 클래스
    - [application.properties](http://application.properties) / application.yml
        - 구성 속성을 지정할 수 있다.
    - static
        - 브라우저에 제공할 정적인 콘텐츠(이미지, 스타일시트, 자바스크립트 등)를 둘 수 잇는 폴더.
        - 처음엔 비어있다.
    - templates
        - 브라우저에 콘텐츠를 보여주는 템플릿 파일을 두는 폴더
        - 처음엔 비어 있지만, Thymeleaf 템플릿을 추가할 수 있다.
    - xxApplicationTests.java
        - 스프링 애플리케이션이 성공적으로 로드되는지 확인하는 간단한 테스트 클래스

- 스프링 부트 스타터(starter) 의존성
    - 자체적으로 라이브러리 코드를 갖지 않고 다른 라이브러리의 것을 사용한다.
    - 스타터 의존성 장점
        - 우리가 필요로하는 모든 라이브러리의 의존성을 선언하지 않아도 되므로 빌드 파일이 훨씬 더 작아지고 관리하기 쉬워진다.
        - 라이브러리 이름이 아닌 기능의 관점으로 의존성을 생각할 수 있다.
        - 라이브러리들의 버전을 걱정하지 않아도 된다. 스프링 부투에 포함되는 라이브러리들의 버전은 호환이 보장되므로 사용하려는 스프링 부트의 버전만 신경쓰면 된다.

- 스프링 부트 플러그인
    - 메이븐은 parent 태그 및 build 태그 하위에 따로 스프링 부트 플러그인을 넣어줘야 한다.
    - Maven

        ```xml
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.4.1</version>
                <relativePath/> <!-- lookup parent from repository -->
        </parent>

        ...

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
        ```

    - Gradle

        ```yaml
        plugins {
            id 'org.springframework.boot' version '2.4.1'
            id 'io.spring.dependency-management' version '1.0.10.RELEASE'
        }
        ```

    - 메이븐을 사용하는 애플리케이션을 실행할 수 있게 해준다.
    - 의존성에 지정된 모든 라이브러리가 실행 가능 JAR 파일에 포함되어 있는지, 그리고 런타임 시에 classpath에서 찾을 수 있는지 확인한다.
    - 실행 가능 JAR 파일의 메인 클래스로 부트스트랩 클래스(xxApplication.java)를 나타내는 매니페스트 파일을 JAR 파일에 생성한다.

- 애플리케이션의 부트스트랩(구동)
    - 실행 가능 JAR 파일에서 애플리케이션을 실행하므로 제일 먼저 시작되는 부트스트랩 클래스가 있어야 한다.

        ```java
        @SpringBootApplication
        public class SpringInActionApplication {
            public static void main(String[] args) {
                SpringApplication.run(SpringInActionApplication.class, args);
            }
        }
        ```

    - 애플리케이션을 부트스트랩하기 위한 최소한의 스프링 구성도 있어야 한다.
    - @SpringBootApplication
        - @SpringBootConfiguration
            - 현재 클래스를 구성 클래스로 지정한다.
            - @Configuration 상속받는 특화된 형태이다.
        - @EnableAutoConfiguration
            - 스프링 부트 자동-구성을 활성화한다.
            - 우리가 필요로 하는 컴포넌트들을 자동으로 구성하도록 스프링 부트에 알려준다.
        - @ComponentScan
            - 컴포넌트 검색을 활성화한다.
            - @Component, @Controller, @Service 등과 함께 클래스를 선언할 수 있게 해준다.
            - 스프링은 자동으로 위를 찾아 스프링 애플리케이션 컨텍스트에 컴포넌트로 등록한다.
    - main()
        - JAR 파일이 실행될 때 호출되어 실행되는 메서드
        - 실제로 애플리케이션을 시작시키고 스프링 애플리케이션 컨텍스트를 생성하는 SpringApplication 클래스의 run() 호출.
        - run()
            - 두 개의 매개변수, 구성 클래스와 명령행(command-line) 인자
        - 구성 클래스가 부트스트랩 클래스와 같아야 하는 것은 아니지만 대개 동일하게 지정한다.

- 애플리케이션 테스트하기

    ```java
    @SpringBootTest
    class SpringInActionApplicationTests {
        @Test
        void contextLoads() {
        }
    }
    ```

    - 스프링 애플리케이션 컨텍스트가 성공적으로 로드될 수 있는지 확인하는 기본적인 검사를 수행한다.
    - @SpringBootTest
        - 스프링 부트 기능으로 테스트를 시작하라는 것을 JUnit에 알려준다.
        - 테스트 메서드가 없다면 아무 일도 하지 않는다.
        - 실행 코드는 없더라도 테스트 메서드가 있는 경우에 스프링 애플리케이션 컨텍스트가 로드된다.

## 03. 스프링 애플리케이션 작성하기

---

- 스프링 MVC
    - 웹 프레임워크
    - 컨트롤러
        - 웹 요청과 응답을 처리하는 컴포넌트(구성 요소)
        - 선택적으로 모델 데이터를 채워서 응답
        - 브라우저에 반환되는 HTML을 생성하기 위해 해당 응답의 웹 요청을 뷰에 전달한다.
    - @Controller

        ```java
        @Controller
        public class HomeController {
            @GetMapping("/")
            public String home() {
                return "home";
            }
        }
        ```

        - 컴포넌트 검색 시 클래스가 컴포넌트로 식별되게 하는 것이 주 목적
        - 클래스를 찾은 후 스프링 애플리케이션 컨텍스트의 빈으로 HomeController의 인스턴스 생성
        - @Component, @Service, @Repository 포함 소수의 다른 애노테이션들도 동일한 기능을 제공하지만, 애플리케이션에서의 역할을 더 잘 설명해준다.
        - @GetMapping
            - 루트 경로인 /의 HTTP GET 요청이 수신되면 이 메서드가 해당 요청을 처리해야 한다.
            - 반환하는 String 은 뷰의 논리적인 이름이다.

- 컨트롤러 테스트하기

    ```java
    @WebMvcTest(HomeController.class)
    public class HomeControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testHomePage() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(content().string(containsString("Welcome to...")));
        }
    }
    ```

    - @SpringBootTest 대신 @WebMvcTest 를 사용
    - @WebMvcTest
        - 스프링 부트에서 제공하는 특별한 테스트 애노테이션
        - 스프링 MVC 애플리케이션의 형태로 테스트가 실행되도록 한다.
        - HomeController가 스프링 MVC에 등록되므로 우리가 스프링 MVC에 웹 요청을 보낼 수 있다.
        - 스프링 MVC를 테스트하기 위한 스프링 지원을 설정
            - 실제 서버를 시작하는 대신 스프링 MVC의 모의(Mocking) 메커니즘을 사용
            - 모의 테스트를 하기 위해 테스트 클래스에 MockMvc 객체를 주입하여 수행한다.
    - 스프링 부트 애플리케이션에는 실행에 필요한 모든 것이 포함된다.(내장된 톰캣)

- 스프링 부트 DevTools
    - 스프링 개발자에게 개발 시점의 편리한 도구를 제공
        - 코드가 변경될 때 자동으로 애플리케이션을 다시 시작시킨다.
            - DevTools 사용하면 애플리케이션은 JVM에서 두 개의 클래스 로더(loader)에 의해 로드된다.
            - 클래스 로더1: 자바 코드, 속성 파일, src/main/ 경로의 모든 것과 함께 로드
            - 클래스 로더2: 자주 변경되지 않는 의존성 라이브러리와 함께 로드
            - 단점: 빌드 명세 변경 시, 자동으로 의존성 변경이 적용될 수 없다.
        - 브라우저로 전송되는 리소스(ex> 템플릿, 자바스크립트, 스타일시트)가 변경될 때 자동으로 브라우저를 새로고침한다.
        - 템플릿 캐시를 자동으로 비활성화한다.
        - 만일 H2 DB가 사용 중이라면 자동으로 H2 콘솔을 활성화한다.
    - 개발 시에만 사용되도록 했으므로 실제 운영에서는 스스로 비활성화된다.

## 04. 스프링 살펴보기

---

- 핵심 스프링 프레임워크
    - 핵심 컨테이너와 의존성 주입 프레임워크 제공
    - 스프링의 웹 프레임워크, 스프링 MVC 제공
    - 템플릿 기반의 JDBC(JdbcTemplate) 및 기본적인 데이터 퍼시스턴스 지원 제공
    - 리액티브 프로그래밍 지원(스프링 MVC 개념의 스프링 WebFlux 리액티브 웹 프레임워크)

- 스프링 부트
    - 스타터 의존성과 자동-구성 제공
    - 액추에이터(Actuator) 제공
        - 애플리케이션 내부 작동을 런타임 시에 살펴볼 수 있는 기능
        - 메트릭(metric), 스레드 덤프 정보, 애플리케이션 상태, 애플리케이션에서 사용할 수 있는 환경 속성
    - 환경 속성의 명세
    - 핵심 프레임워크에 추가되는 테스트 지원
    - 스프링 부트 CLI(command-line interface)(명령행 인터페이스) 제공

- 스프링 데이터
    - 핵심 스프링 프레임워크에도 기본적인 데이터 퍼시스턴스 지원하지만,
    - 스프링 데이터는 간단한 자바 인터페이스로 우리 애플리케이션의 데이터 리퍼지터리 정의 가능
    - 데이터를 저장하고 읽는 메서드를 작명 규칙을 사용해서 정의
    - 서로 다른 종류 DB와 함께 사용 가능

- 스프링 시큐리티
    - 인증(authentication)
    - 허가(authorization)
    - API 보안을 포함하는 폭넓은 범위의 애플리케이션 보안 요구를 다룬다.

- 스프링 통합과 배치
    - 스프링 통합: 데이터가 사용 가능한 즉시 처리되는 실시간 통합을 한다.
    - 스프링 배치: 다량의 데이터가 처리되는 시점을 트리거가 알려줄 때 데이터가 수집 처리되는 배치 통합을 처리해 준다.

- 스프링 클라우드
    - 마이크로서비스 아키텍쳐(MSA): 애플리케이션을 여러 개의 개별적인 단위들로 합성하는 것

https://www.notion.so/Chapter-1-cf642ce1f2b140209b5fb99fa68dfe12
