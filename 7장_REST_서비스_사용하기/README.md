# Chapter 7. REST 서비스 사용하기

- 스프링 애플리케이션 내 REST API 사용
    - RestTemplate

        스프링 프레임워크에서 제공하는 간단하고 동기화된 REST 클라이언트

    - Traverson

        스프링 HATEOAS에서 제공하는 하이퍼링크를 인식하는 동기화 REST 클라이언트로 같은 이름의 자바스크립트 라이브러리로부터 비롯된 것이다.

    - WebClient

        스프링 5에서 소개된 반응형 비동기 REST 클라이언트

    - Feign Client
        - Manual하게 만들어 쓸 수도 있다. 참고용 코드. 연차 [https://rbye.now.sh/t/server](https://rbye.now.sh/t/server)

            ```java
            @RestController
            @Import(FeignClientsConfiguration.class)
            public class ManualFeignController {

                private SimpleClient clientManual;

                public ManualFeignController(Contract contract) {
                    this.clientManual =  Feign.builder()
                                              .contract(contract)
                                              .decoder((response, type) -> new SimpleClientData(7, "manual", 10))
                                              .target(SimpleClient.class, "http://localhost:8081");
                }

                @GetMapping("/data/manual/{id}")
                public SimpleClientData getManualData(@PathVariable int id) {
                    return clientManual.getData(id);
                }
            }
            ```

        - MSA 에 많이 쓰인다.
- MSA(Microservice Architecture)
    - Monolithic 아키텍처
        - 오랫동안 많은 개발자들이 좋아하며 사용한 아키텍처
        - 하나의 커다란 서비스와 저장소로 구성된 이 아키텍처는 하나의 모듈이 변경되면 전체 서버를 배포해야 했다.
        - 작은 기능의 문제점도 곧 서버 전체의 문제점이 되었다.
        - 많은 코드가 엮여 있기 때문에 변화하는 비지니스의 요구 사항에 유연하게 대처하지 못했다.
    - MSA
        - Netflix에서 전격적으로 적용한 아키텍처
        - 커다란 서비스는 작은 단위의 독립된 모듈로 쪼개지고, 추가 기능이나 확장이 필요하면 해당 모듈만 변경하면 된다.
        - 많은 개발자들은 기존 Monolithic 아키텍처를 MSA로 바꾸어가기 시작.
        - 단점
            - 단일 애플리케이션 내부의 메서드 콜을 통한 정보 교환 → 네트워크를 타고 모듈 간의 정보 교환으로 변경됨.
            - 스레드 풀을 이용한 동기식 호출 방식은 코드가 간단하고 순차적으로 동작하기 때문에 개발자가 코드를 직관적이고 빠르게 작성할 수 있지만 이렇게 작성한 코드로 만든 서버도 빠르게 동작할까?
    - 동기식 호출 방식
        - 상대편의 응답이 올 때까지 스레드는 기다려야(blocking) 한다.
        - 만약 응답이 늦게 오면 서버가 요청에 대한 응답을 기다리는 데 스레드를 모두 소진해서 추가 요청을 처리할 수 없는 상태가 될 수 있다.
        - 특히 MSA 에서는 타임아웃이 발생할 정도의 지연이 발생하면 순식간에 다른 모듈로 전파되어 전체 시스템이 마비되는 등의 악영향을 끼칠 수 있다.
    - 이벤트 루프를 이용한 비동기 프로그래밍
        - 스레드가 응답을 기다리지 않고 다른 일을 처리하다가 응답이 왔을 때 해당 일을 처리한다면 응답만 기다리면서 불필요하게 리소스를 점유하는 일은 없을 것이다.
        - 이벤트 루프를 활용하면 요청을 보내고 응답이 올 때까지 무작정 기다리는 대신, 자신에게 할당된 다른 여러 소켓의 요청을 순차적으로 빠르게 처리할 수 있다.
        - 서버와 클라이언트의 스레드는 더이상 blocking되지 않는다.
    - Spring WebFlux
        - Spring 생태계에서 지원하는 비동기 프로그래밍

- REST API의 대체제
    - gRPC
        - LINE에서 개발 중인 오픈소스 [Armeria](https://d2.naver.com/helloworld/6080222) 도 사용. 애플리케이션의 성능 향상을 위해 HTTP 대신 RPC를 도입하려다 보니 Spring Framework에서는 RPC(gRPC, Thrift)를 사용하는 것이 상당히 제한적이라 쓰게 되었다고..
        - Protocol Buffer의 encode/decode를 지원하기는 하지만 제한적이다. ([참고](https://spring.io/blog/2015/03/22/using-google-protocol-buffers-with-spring-mvc-based-rest-services))
        - 'Provide gRPC support [[SPR-16358](https://github.com/spring-projects/spring-framework/issues/20905)]' 이슈에서는 아래 두 artifact 중 하나의 사용을 권장한다.
            - [https://github.com/LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)
            - [https://github.com/yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
        - reactive-grpc는 RxJava와 Project Reactor를 지원하지만 Spring과 함께 쓰기 어렵다.
        - 개념: [https://ooeunz.tistory.com/130](https://ooeunz.tistory.com/130)
        - Spring Cloud
            - [https://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/2.1.0.RC2/multi/multi__integrations.html](https://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/2.1.0.RC2/multi/multi__integrations.html)
            - [https://knight76.tistory.com/entry/zipkin-과-spring-cloud-sleuth-소개](https://knight76.tistory.com/entry/zipkin-%EA%B3%BC-spring-cloud-sleuth-%EC%86%8C%EA%B0%9C)
        - RPC 의 또 다른 종류인 thrift ?
            - ServletRegistrationBean을 이용하여 TServlet을 등록하거나 별도의 TServer를 선언해야 한다.
                - WebFlux는 Servlet 기반이 아니므로 ServletRegistrationBean으로 등록할 수 없다.
                - TServer는 별도의 서버 구현체를 만들어야 하기 때문에 별도의 포트를 점유한다.
    - ...

## 01. RestTemplate으로 REST 엔드포인트 사용하기

---

- RestTemplate
    - TRACE를 제외한 표준 HTTP 메서드 각각에 대해 최소한 하나의 메서드를 갖고 있다.

        execute(), exchange()는 모든 HTTP 메서드의 요청을 전송하기 위한 저수준 범용 메서드를 제공한다.

    - 12개 메서드
        - delete(..): 지정된 URL의 리소스에 HTTP DELETE 요청을 수행한다.
        - exchange(..)

            지정된 HTTP 메서드를 URL에 대해 실행하며, 응답 몸체와 연결되는 객체를 포함하는 ResponseEntity를 반환한다.

            - ParameterizedTypeReference: [https://umbum.dev/925](https://umbum.dev/925)
        - execute(..): 지정된 HTTP 메서드를 URL에 대해 실행하며, 응답 몸체와 연결되는 객체를 반환한다.
        - getForEntity(..): HTTP GET 요청을 전송하며, 응답 몸체와 연결되는 객체를 포함하는 ResponseEntity를 반환한다.
        - getForObject(..)
            - HTTP GET 요청을 전송하며, 응답 몸체와 연결되는 객체를 반환한다.
        - headForHeaders(..): HTTP HEAD 요청을 전송, 응답 몸체와 연결되는 객체를 포함하는 ResponseEntity를 반환한다.
        - optionsForAllow(..): HTTP OPTIONS 요청을 전송, 지정된 URL의 Allow 헤더를 반환한다.
        - patchForObject(..): HTTP PATCH 요청을 전송, 응답 몸체와 연결되는 결과 객체를 반환한다.
        - postForEntity(..): URL에 데이터를 POST하며, 응답 몸체와 연결되는 객체를 포함하는 ResponseEntity를 반환한다.
        - postForLocation(..): URL에 데이터를 POST하며, 새로 생성된 리소스의 URL을 반환한다.
        - postForObject(..): URL 에 데이터를 POST하며, 응답 몸체와 연결되는 객체를 반환한다.
        - post(..): 리소스 데이터를 지정된 URL에 PUT한다.
    - 위의 메서드들은 다음 3가지 형태로 오버로딩 되어있다.
        1. 가변 인자 리스트에 지정된 URL 매개변수에 URL문자열(String 타입)을 인자로 받는다.
        2. Map<String, String>에 지정된 URL 매개변수에 URL 문자열을 인자로 받는다.
        3. java.net.URI를 URL에 대한 인자로 받으며, 매개변수화된 URL은 지원하지 않는다.
- ResponseEntity

    getForObject() 는 응답 결과를 나타내는 도메인 객체를 반환하지만, getForEntity()는 ResponseEntity 객체를 반환한다.

     HTTP 요청(Request) 또는 응답(Response)에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스

    ```java
    public class HttpEntity<T> {

    	private final HttpHeaders headers;

    	@Nullable
    	private final T body;
    }
    ```

    ```java
    public class ResponseEntity<T> extends HttpEntity<T>
    ```

    - HttpStatus, HttpHeaders, HttpBody를 포함한다.

- 리소스 가져오기(GET)

    ```java
    public Ingredient getIngredientById(String ingredientId) {
        return restTemplate.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);
    }
    ```

    - 여기서는 URL 변수의 가변 리스트와 URL 문자열을 인자로 받게 오버로딩된 getForObject 사용.
    - 두 번째 매개변수는 응답이 바인딩되는 타입인데, 여기를 참조하여 JSON 형식인 응답 데이터가 객체로 deserialized 되어 반환된다.
    - Deserialized: [https://www.notion.so/ObjectMapper-05d8cc40de74493ab7aba6964202fe31](https://www.notion.so/ObjectMapper-05d8cc40de74493ab7aba6964202fe31)

    - Map에 지정된 URL 매개변수에 URL 문자열을 인자로 받는 케이스

    ```java
    public Ingredient getIngredientById(String id){
    		Map<String, String> urlVariables = new HashMap<>();
    		urlVariables.put("id", id);
    		return restTemplate.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, urlVariables);
    }
    ```

    - URI를 URL에 대한 인자로 받으며, 매개변수화된 URL은 지원하지 않는다.

    ```java
    public Ingredient getIngredientById(String id){
    		Map<String, String> urlVariables = new HashMap<>();
    		urlVariables.put("id", id);
    		URI url = UriComponentsBuilder
                     .fromHttpUrl("http://localhost:8080/ingredients/{id}")
                     .build(urlVariables);
    		return restTemplate.getForObject(url, Ingredient.class);
    }
    ```

- 리소스 쓰기(PUT)
    - HTTP PUT 요청 전송하기 위해 RestTemplate은 put() 메서드를 제공. 3개의 오버로딩 버전이 있음.
    - 직렬화된 후 지정된 URL로 전송되는 Object 타입을 인자로 받는다.

    ```java
    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put("http://localhost:8080/ingredients/{id}", ingredient, ingredient.getId());
    }
    ```

- 리소스 삭제하기(DELETE)
    - HTTP DELETE 요청

    ```java
    public void deleteIngredient(Ingredient ingredient) {
        restTemplate.delete("http://localhost:8080/ingredients/{id}",
                ingredient.getId());
    }
    ```

- 리소스 데이터 추가하기(POST)
    - 주로 추가 요청에 사용되지만..., HTTP웹 기본지식 참고자료

    ```java
    public Ingredient createIngredient(Ingredient ingredient) {
        return restTemplate.postForObject("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }
    ```

## 02. Traverson으로 REST API 사용하기

---

Traverson은 스프링 데이터 HATEOAS에 같이 제공되며, 스프링 애플리케이션에서 하이퍼미디어 API를 사용할 수 있는 솔루션.

- 해당 API의 기본 URI를 갖는 객체를 생성해야 한다.

    ```java
    Traverson traverson = new Traverson(
                    URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
    ```

- 참고

    Spring HATEOAS 는 이 순간에도 계속 변경되고 있다.

    [https://docs.spring.io/spring-hateoas/docs/current/reference/html/#reference](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#reference)