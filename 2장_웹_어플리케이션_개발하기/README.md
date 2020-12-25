# Chapter 2. 웹 애플리케이션 개발하기

## 01. 정보 보여주기

---

- 도메인: 애플리케이션의 이해에 필요한 개념을 다루는 영역

- @Slf4j
    - 컴파일 시에  Lombok에 제공되며, 이 클래스에 자동으로 SLF4J Logger를 생성한다.
    - 다음 코드를 추가한 것과 같은 효과를 낸다.

        ```java
        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);
        ```

- 스프링 MVC 요청
    - @RequestMapping: 다목적 요청을 처리한다.
    - @GetMapping: HTTP GET 요청을 처리한다.
    - @PostMapping
    - @PutMapping
    - @DeleteMapping
    - @PatchMapping

- 스프링 부트 애플리케이션 실행하는 방법
    - 애플리케이션을 실행 가능한 JAR 파일로 빌드한 후 java -jar로 JAR 실행
    - mvn spring-boot:run
    - 스프링 부트 대시보드 사용

## 03. 폼 입력 유효성 검사하기

---

- JSR-303
    - 스프링은 자바의 빈 유효성 검사(Bean Validation) API를 지원한다.
    - 스프링 부트를 사용하면 유효성 검사 라이브러리를 쉽게 추가할 수 있다.
    - 유효성 검사 API와 이를 구현한 Hibernate 컴포넌트는 스프링 부트의 웹 스타터 의존성으로 자동 추가되기 때문이다.

      → 위의 사항은 스프링부트 2.2 까지만. 스프링 2.3부터 spring-boot-starter-validation은 제거됨 [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes)

    - 참고
        - 스프링 부트 2.2 부터
            - Gradle 4.10 이상 필요
            - Spring Framework 5.2
            - javax → jakarta
            - spring-boot-starter-test 디폴트 JUnit5로 변경
            - Elasticsearch 6.7 사용
            - Java 13 지원
        - 스프링 부트 2.3 부터
            - Gradle 6.3 이상 필요
            - Spring Data Neumann
                - Major Spring Data release
            - Multiple profiles with @ActiveProfiles

                ```java
                @ActiveProfiles({"p1","p2"})
                ```

            - 모든 내장 웹 서버에 대하여 Graceful shutdown 지원
                - Jetty, Reactor Netty, Tomcat, Undertow
                - reactive와 Servlet-based 웹 어플리케이션 둘 다 지원.

                ```yaml
                server.shutdown=graceful
                ```

            - Elasticsearch 7.5 이상 사용
            - Java 14 지원
        - 스프링 부트 2.4 부터
            - Gradle
            - Spring Framework 5.3
            - Spring Data 2020.0
                - Major Spring Data release
            - spring-boot-starter-test 에서 JUnit5의 Vintage Engine이 제거된다.
                - JUnit4를 계속 쓰고 싶으면 Vintage Engine 을 추가해야 한다.

                    ```xml
                    <dependency>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                        <scope>test</scope>
                        <exclusions>
                            <exclusion>
                                <groupId>org.hamcrest</groupId>
                                <artifactId>hamcrest-core</artifactId>
                            </exclusion>
                        </exclusions>
                    </dependency>
                    ```

                    ```yaml
                    testImplementation("org.junit.vintage:junit-vintage-engine") {
                        exclude group: "org.hamcrest", module: "hamcrest-core"
                    }
                    ```

            - Elasticsearch RestClient 자동 구성 종료
                - RestHighLevelClient 빈은 자동 구성 계속 지원된다.
            - Java 15 지원

- 유효성 검사 애노테이션
    - @NotNull
    - @Size
    - @NotBlank
    - @CredentialCardNumber
    - @Pattern
    - @Digits
    - 모든 유효성 검사 애노테이션은 message 속성이 있다. 유효성 규칙 충족하지 못할 때 보여줄 메시지.

- 유효성 검사 수행
    - @Valid
    - 객체가 바인딩 되면서 유효성 검사를 수행하라고 스프링 MVC에 알려준다.
    - 검사 에러 있으면 Errors 객체에 저장된다.
    - errors.hasError() 로 검사 에러가 있는지 확인한다.

## 04. 뷰 컨트롤러로 작업하기

---

- 뷰 컨트롤러 선언하기

    ```java
    @Configuration
    public class WebMvcConfig implements WebMvcConfigurer {
    		@Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("home");
        }
    }
    ```

    - WebMvcConfigurer
        - 스프링 MVC를 구성하는 메서드를 정의
        - 인터페이스임에도 불구하고, 정의된 모든 메서드의 기본적인 구현을 제공한다.
        - 필요한 메서드만 선택해서 오버라이딩하면 된다.
    - addViewControllers
        - 하나 이상의 뷰 컨트롤러를 등록하기 위해 사용할 수 있는 ViewControllerRegistry를 인자로 받는다.
        - 이로써 HomeController를 대체할수 있다.
    - 부트스트랩 구성 클래스 xxApplication에 위를 추가해도 된다. 그러나 서로 다른 종류의 구성 클래스는 새로 생성하는 것이 좋다.

https://www.notion.so/Chapter-2-9627f99bebc94f53b0cb51fe41b83480
