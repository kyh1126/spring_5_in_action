# 6. REST 서비스 생성하기

클라이언트가 모바일, 태블릿, 워치 등등 다양해짐

→ 다양한 클라이언트가 백엔드와 상호작용할 수 있는 **API**를 제공해야 해!

→ REST가 필요해..!

- **REST?**

    Representational State Transfer의 약자

    - 리소스를 이름으로 구분해서(Representiational) 해당 리소스의 상태(State)를 주고 받는(Transfer) 모든 것
    - http uri을 통해 리소스를 명시하고 method를 통해 리소스에 대한 crud operation을 적용

    **구성 요소**

    - 리소스
        - HTTP URI
        - 모든 리소스에 고유한 id가 존재하고, 이 리소스는 서버에 존재한다
        - 리소스를 구별하는 id는 HTTP URI (/tacos/:id)
        - 클라이언트는 URI로 리소스를 지정하고 해당 리소스의 상태에 대한 조작을 서버에게 요청함
    - 행위
        - HTTP 프로토콜의 Method
        (Get/Post/Put/Delete과 같은 메소드)
    - 표현 (리소스의 표현)
        - 클라이언트가 리소스의 상태(정보)에 대한 조작을 요청하면 서버는 이에 적절한 응답(표현)을 보낸다
        - 주로 json을 통해 데이터를 주고받음

    **주요 특징**

    - 서버-클라이언트 구조
    - stateless (상태가 없음)
        - 클라이언트의 context를 서버에 저장하지 않음
        (세션/쿠키와 같은 context 정보를 신경쓰지 않아도 됨)
        - 서버는 각각의 요청을 별개의 것으로 인식하고 처리
    - 인터페이스 일관성
        - 리소스에 대한 조작을 동일한 방식으로 수행

    **REST API**

    REST 기반으로 서비스 API를 구현한 것

    REST API 설계 규칙 ([https://dzone.com/articles/7-rules-for-rest-api-uri-design-1](https://dzone.com/articles/7-rules-for-rest-api-uri-design-1))

    1. 계층 관계는 슬래시(/)로
    2. URI 마지막 문자로 슬래시(/ )를 포함하지 않는다.
    3. 하이픈(-)은 URI 가독성을 높이는데 사용
    4. 언더바(_)은 URI에 사용하지 않음
    5. URI 경로에는 소문자가 적합
    6. 파일확장자는 URI에 포함하지 않음
    7. url의 리소스 간에는 연관 관계가 있는 경우 계층 관계를 이용

# REST 컨트롤러 작성하기

예제에서는 Angular Framework를 사용해서 SPA로 Front-End를 구축

- What is SPA?

    Single Page Application

    프레젠테이션 계층이 백엔드와 거의 독립적
    (백엔드를 그대로, But UI만 다르게 개발할 수 있음)

    → 다른 어플리케이션과 통합하기 쉬움

    (웹 페이지에 정보를 보여주는 것이 전부라면 MPA가 더 간단할 수 있음)

[스프링 MVC의 HTTP 요청-처리 어노테이션](6%20REST%20%E1%84%89%E1%85%A5%E1%84%87%E1%85%B5%E1%84%89%E1%85%B3%20%E1%84%89%E1%85%A2%E1%86%BC%E1%84%89%E1%85%A5%E1%86%BC%E1%84%92%E1%85%A1%E1%84%80%E1%85%B5%20bec791aa76b24283b0de9a21567b79a9/%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%E1%84%8B%E1%85%B4%20HTTP%20%E1%84%8B%E1%85%AD%E1%84%8E%E1%85%A5%E1%86%BC-%E1%84%8E%E1%85%A5%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A5%E1%84%82%E1%85%A9%E1%84%90%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20e22891c8a56342be8d14315344ac2d6e.csv)

생성, 읽기, 변경, 삭제를 하는 CRUD 연산과 HTTP 메서드가 1:1로 완벽 대응되는 것이 아님

### 서버에서 데이터 가져오기

(데이터 흐름 : Server → Client)

```java
**@RestController
@RequestMapping(**path = "/design", **produces = "application/json")
@CrossOrigin**(origins = "*")
@RequiredArgsConstructor
public class DesignTacoController {
    private final TacoJpaRepository tacoRepository;

    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(page).getContent();
    }

    @GetMapping(**"/{id}"**)
    public **ResponseEntity<Taco>** tacoById(**@PathVariable("id")** Long id) {
        Optional<Taco> optTaco = tacoRepository.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), **HttpStatus.OK**);
        }
        return new ResponseEntity<>(null, **HttpStatus.NOT_FOUND**);
    }

}
```

**@RestController**

1. 스프링 컴포넌트 검색의 대상이 되게 해줌
2. http 응답 body에 (html로 렌더링되지 않고) 반환값을 ****직접 http 응답으로 작성

다른 방법?

1. @Controller + @ResponseBody
(@Controller를 달 때는 메소드에 @ResponseBody 어노테이션 지정)
2. ResponseEntity 객체를 반환함

**@RequestMapping**

produces

- Accept 헤더에 "application/json"이 포함된 요청만을 DesignTacoController의 메소드에서 처리하겠다
- 여러개 설정 가능 like produces={"application/json","text/xml"}

**@CrossOrigin**

- CORS란?

    동일 출처 정책 : 어떤 출처에서 불러온 문서나 스크립트가 다른 출처에서 가져온 리소스와 상호작용하는 것을 막음

    동일 출처 정책 때문에 Cross Domain 이슈가 발생

    Cross-Origin Resource Sharing : Cross Domain 이슈와 관련하여, 웹 브라우저에서 외부 도메인 서버와 통신하기 위한 방식을 표준화한 스펙

    정해진 헤더를 통해 서버와 클라이언트가 서로 요청이나 응답에 반응할지 결정

    [https://developer.mozilla.org/ko/docs/Web/HTTP/CORS](https://developer.mozilla.org/ko/docs/Web/HTTP/CORS)

다른 도메인의 클라이언트에서 해당 REST API를 사용할 수 있게 해줌

**@PathVariable**

path의 플레이스홀더 담겨있는 값이 지정됨

**ResponseEntity**

상태 코드를 지정하여 반환할 수 있음

id 조회 시, 값이 없을 때 null을 반환하는 것은 좋은 방법이 아님
(콘텐츠가 없는데도 정상 처리를 하니까)

### 서버에 데이터 전송하기

(데이터 흐름 : Client → Server)

```java
:
public class DesignTacoController {
    :

    @PostMapping(**consumes** = "application/json")
    **@ResponseStatus(HttpStatus.CREATED)**
    public Taco postTaco(**@RequestBody** Taco taco) {
        return tacoRepository.save(taco);
    }

}
```

**@PostMapping(consumes = "application/json")**

Content-type이 application/json과 일치하는 요청만 처리

**@ResponseStatus(HttpStatus.CREATED)**

요청이 성공 + 리소스가 정상적으로 생성 → 201(CREATED)로 응답

(클라이언트에게 더 서술적이며 정확한 상태코드를 전달하는 것이 더 좋음)

- success인 http status들
    - OK
        - GET : 메시지 바디에 리소스가 정상적으로 담겼다
        - HEAD : 개체 헤더 정보가 바디에 담겼다
        - PUT/POST : 수행 결과가 메소드 바디에 담겼다
        - TRACE : 서버에서 수신한 요청 메시지를 바디에 포함했다
    - CREATED
        - 요청 성공, 새로운 리소스가 생겼다
    - ACCEPTED
        - 요청 처리에 대한 결과를 당장 줄 수 없음
        (비동기 처리 또는 다른 서버나 배치 작업에게 요청을 넘긴 경우에 사용됨)
    - NON_AUTHORITATIVE_INFORMATION
        - 돌려받은 메타 정보 세트가 오리진 서버의 것과 일치하지 않지만 로컬이나 서드 파티 복사본에서 모아졌음
        (이러한 조건에서는 200 OK 응답 우선)
    - NO_CONTENT
        - 성공했지만 보내줄 콘텐츠가 없음
    - RESET_CONTENT
        - 요청을 보낸 문서 뷰를 리셋하라는 의미
    - PARTIAL_CONTENT
        - 클라이언트에서 복수의 스트림을 분할 다운로드하고자 할 때 사용됨
    - MULTI_STATUS
        - 여러 리소스가 여러 상태 코드인 상황
    - ALREADY_REPORTED
        - DAV에서 사용됩니다
        - 응답 속성으로 동일 컬렉션으로 바인드된 복수의 내부 멤버를 반복적으로 열거하는 것을 피하기 위해 사용됨
    - IM_USED
        - 서버가 GET 요청에 대한 리소스의 의무를 다 했음
        그리고 응답이 하나 또는 그 이상의 인스턴스 조작이 현재 인스턴스에 적용이 되었음

**@RequestBody**

request body의 json 데이터가 Taco 객체로 변환되어 taco 매개변수와 바인딩됨

지정하지 않으면 쿼리나 폼 매개변수가 바인딩됨

### 서버에 데이터 변경하기

데이터를 변경하는 2가지 방법

1. 데이터 전체 변경
2. 데이터 일부 변경

```java
@RestController
@RequestMapping(path = "/order")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderJpaRepository orderRepository;

    **@PutMapping**("/{orderId}")
    public Order putOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    **@PatchMapping**(path = "/{orderId}", consumes = "application/json")
    public Order patchOrder(@PathVariable("orderId") Long orderId, @RequestBody Order patch) {
        Order order = orderRepository.findById(orderId).get();

        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(order.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(order.getDeliveryStreet());
        }

				:

        if (patch.getCcCVV() != null) {
            order.setCcCVV(order.getCcCVV());
        }
        return orderRepository.save(order);
    }

}
```

**@PutMapping**

해당 데이터 전체를 변경

**@PatchMapping**

데이터의 일부를 변경하고자 할 때 사용

클라이언트에서는 변경할 속성만 전송
서버에서는 클라이언트에서 지정한 속성의 데이터만 변경하고, 지정하지 않은 속성의 데이터를 그대로 유지
→ Put에 비해 변경 작업이 복잡

Patch 메소드의 2가지 제약

1. 클라이언트가 null로 변경하고 싶다는 의사를 알아낼 방법이 필요
2. 컬렉션의 항목을 삭제/추가하려면 변경될 컬렉션 데이터 전체를 전송해야 함

꼭 도메인 데이터로 변경할 필요는 없다!
도메인 데이터 대신, 패치할 변경사항 명세를 전송할 수 있음
→ 그러려면 도메인 데이터 대신 패치 명세를 처리하도록 요청 처리 메소드가 필요

### 서버에서 데이터 삭제하기

```java
**@DeleteMapping**("/{orderId}")
**@ResponseStatus(code = HttpStatus.NO_CONTENT)**
public void deleteOrder(@PathVariable("orderId") Long orderId) {
    try {
        orderRepository.deleteById(orderId);
    } catch (EmptyResultDataAccessException e) {
				// 아무것도 하지 않음
    }
}
```

**@DeleteMapping**

DELETE 요청을 처리

**HttpStatus.NO_CONTENT**

리소스를 삭제하는 것이므로 클라이언트에게 데이터를 반환할 필요가 없음
(일반적으로 DELETE 요청은 body data를 갖고있지 않음)

따라서 반환 데이터가 없다는 것을 클라이언트가 알 수 있게 http 상태 코드를 사용

**비어있는 catch 블록**

삭제를 요청한 리소스가 없을 경우, 처리 방법

1. 특별한 처리 X → 삭제된 것처럼 특별히 해줄 것이 없다고 판단
2. NOT_FOUND 상태 코드를 반환 → ResponseEntity를 사용하여 상태 코드 명시

# 하이퍼미디어 사용하기

클라이언트가 api를 호출할 때 url을 직접 구성하여 호출한다

→ 클라이언트 코드에 api의 url 스킴이 박혀있다

→ url 스킴이 변경되면 클라이언트에게 문제가 있다

→ 클라이언트 코드가 api의 스킴에 의존적이다

해결하는 한가지 방법 : HATEOAS

→ 클라이언트의 코드로부터 url 스킴을 분리함 (의존성 제거)

api로부터 반환되는 리소스에 해당 리소스와 관련된 하이퍼링크들이 포함됨
클라이언트가 최소한의 api url만 알면, 반환되는 리소스 관련 다른 api url들을 사용

- Before & After

    Before

    ```json
    [
      {
        "id": 1,
        "createdAt": "2021-01-06T21:00:43.076+00:00",
        "name": "wanda-taco",
        "ingredients": [
          {
            "id": "FLTO",
            "name": "Flour Tortilla",
            "type": "WRAP"
          },
          {
            "id": "TMTO",
            "name": "Diced Tomatoes",
            "type": "VEGGIES"
          }
        ]
      }
    ]
    ```

    After

    ```json
    {
      "_embedded": {
        "tacos": [
          {
            "name": "wanda-taco",
            "createdAt": "2021-01-06T21:00:01.423+00:00",
            "ingredients": {
              "_embedded": {
                "ingredients": [
                  {
                    "name": "Flour Tortilla",
                    "type": "WRAP",
                    "_links": {
                      "self": {
                        "href": "http://localhost:8080/ingredients/FLTO"
                      }
                    }
                  },
                  {
                    "name": "Diced Tomatoes",
                    "type": "VEGGIES",
                    "_links": {
                      "self": {
                        "href": "http://localhost:8080/ingredients/TMTO"
                      }
                    }
                  }
                ]
              }
            },
            "_links": {
              "self": {
                "href": "http://localhost:8080/design/1"
              }
            }
          }
        ]
      },
      "_links": {
        "recents": {
          "href": "http://localhost:8080/design/recent"
        }
      }
    }
    ```

json 응답에 하이퍼링크를 포함시킬 때 주로 사용하는 형식 (HAL)

**스프링의 스타터 의존성**

- dependency

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-hateoas</artifactId>
    </dependency>
    ```

    ```bash
    implementation 'org.springframework.boot:spring-boot-starter-hateoas'
    ```

1. 스프링 hateoas를 classpath에 추가
2. 스프링 hateoas를 활성화하는 auto-configuration 제공

우리가 할 건, 도메인 타입 대신 모델 타입(~~이전에는 리소스~~)을 반환하도록 컨트롤러를 수정하면 됨

- 그전에, hateoas 변경사항 링크 참고

    [https://docs.spring.io/spring-hateoas/docs/current/reference/html/#migrate-to-1.0.changes](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#migrate-to-1.0.changes)

### 하이퍼링크 추가하기

```java
@GetMapping("/recent")
public **CollectionModel<TacoModel>** recentTacos() {
    PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
    List<Taco> tacos = tacoRepository.findAll(page).getContent();

    **CollectionModel<TacoModel> recentModels = new TacoModelAssembler().toCollectionModel(tacos);**
    recentModels.**add**(
            **WebMvcLinkBuilder.linkTo**(
                    WebMvcLinkBuilder.**methodOn**(DesignTacoController.class).recentTacos()).**withRel**("recents"));
    return recentModels;
}
```

**linkTo()** 

url을 하드코딩하는 것은 좋지 않음

스프링에서는 링크 빌더를 제공

WebMvcLinkBuilder를 통해 컨트롤러의 기본 url에 관련된 링크의 빌드를 도와줌

**methodOn()**

메소드의 매핑 경로도 빌딩 가능

**withRel()**

이 링크의 relation name

**TacoModel**

RepresentationModel를 상속/오버라이드

RepresentationModel? 다른 리소스를 링크할 수 있는 객체

- **TacoModel/IngredientModel** 코드보기
    - **TacoModel**

        ```java
        **@Relation(value = "taco", collectionRelation = "tacos")**
        public class TacoModel **extends RepresentationModel<TacoModel>** {

            private static final IngredientModelAssembler ingredientAssembler = new IngredientModelAssembler();

            @Getter
            private final String name;
            @Getter
            private final Date createdAt;
            @Getter
            private final CollectionModel<IngredientModel> ingredients;

            public TacoModel(Taco taco) {
                this.name = taco.getName();
                this.createdAt = taco.getCreatedAt();
                this.ingredients = ingredientAssembler.toCollectionModel(taco.getIngredients());
            }
        }
        ```

    - **IngredientModel**

        ```java
        **@Relation(value = "ingredient", collectionRelation = "ingredients")**
        public class IngredientModel **extends RepresentationModel<IngredientModel>** {

            @Getter
            private final String name;

            @Getter
            private final Type type;

            public IngredientModel(Ingredient ingredient) {
                this.name = ingredient.getName();
                this.type = ingredient.getType();
            }
        }
        ```

**@Relation**

최상위 수준의 요소 _embedded 밑에 relation name을 지정

지정하지 않으면 클래스 이름에 따라 relation name이 달라진다 (List<TacoModel>)

자바로 정의된 클래스 이름과 json 필드 이름간의 결합도를 낮추기 위해 지정

- **?!** Taco 객체에 바로 상속하면 안되나요?

    도메인과 리소스를 결합하는 게 좋을까?
    → 음, 전 아니 (좋다고 생각하는 개발자들도 있는 것 같은데..)

    링크가 필요없는 경우도 있고,

    도메인과 리소스를 분리하면 도메인의 id가 노출되지 않게 할 수 있음
    (db에서 필요한 id를 api에 노출시킬 필요가 없다) 

**CollectionModel**

RepresentationModel과 동일하게 다른 리소스를 링크

컬렉션의 경우, 반복문으로 하나하나 래핑할수는 없음 (~~번거로와..)~~

스프링에서는 컬렉션을 위한 CollectionModel 지원

**TacoModelAssembler**

RepresentationModelAssemblerSupport를 상속/오버라이드

- **TacoModelAssembler/IngredientModelAssembler** 코드보기
    - **TacoModelAssembler**

        ```java
        public class TacoModelAssembler extends RepresentationModelAssemblerSupport<Taco, TacoModel> {

            public TacoModelAssembler() {
                super(DesignTacoController.class, TacoModel.class);
            }

            @Override
            protected TacoModel instantiateModel(Taco taco) {
                return new TacoModel(taco);
            }

            @Override
            public TacoModel toModel(Taco taco) {
                return createModelWithId(taco.getId(), taco);
            }
        }
        ```

    - **IngredientModelAssembler**

        ```java
        public class IngredientModelAssembler extends **RepresentationModelAssemblerSupport**<Ingredient, IngredientModel> {

            public IngredientModelAssembler() {
                super(IngredientController.class, IngredientModel.class);
            }

            @Override
            protected IngredientModel instantiateModel(Ingredient ingredient) {
                return new IngredientModel(ingredient);
            }

            @Override
            public IngredientModel toModel(Ingredient ingredient) {
                return createModelWithId(ingredient.getId(), ingredient);
            }
        }
        ```

**생성자 :** Controller와 Model 정보로 super의 생성자를 호출 (링크에 포함되는 url의 기본 경로를 결정하기 위함)

**instantiateResource() :** Model에 기본 생성자가 없을 경우 오버라이딩

**toResource()** : Model을 생성하면서 id속성 값으로 생성되는 self 링크가 url에 자동 지정 (내부적으로 instantiateResource()메소드 호출)

# 데이터 기반 서비스 활성화하기

스프링 데이터를 사용중인 프로젝트라면, 생성된 리파지토리 관련 rest api가 자동으로 생성할 수 있음

→ 스프링 데이터 REST!

start dependency를 추가하기만 하면,
스프링 데이터가 생성한 모든 리파지토리의 rest api가 자동으로 생성되도록 스프링 데이터 rest가 auto-configuration됨

- dependency

    maven

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    ```

    gradle

    ```bash
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    ```

dependency 추가했더니, 만든 적 없는 api들이 모두 생성되어있음

GET localhost:8080/orders

```json
{
  "_embedded": {
    "orders": []
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/orders"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile/orders"
    },
    "search": {
      "href": "http://localhost:8080/api/orders/search"
    }
  }
}
```

GET localhost:8080/users

```json
{
  "_embedded": {
    "users": []
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/users"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile/users"
    },
    "search": {
      "href": "http://localhost:8080/api/users/search"
    }
  }
}
```

우리가 만든 다른 컨트롤러와 충돌하지 않도록, 기본 경로 설정

```yaml
spring.data.rest.base-path: /api
```

- 그 밖의 쉽게 설정 가능한 속성들

    default-media-type : 개별 설정이 없을 때 사용할 Content-Type

    default-page-size : 기본 페이지 사이즈

    detection-strategy : api로 만들 repository를 찾는 전략 ([https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.setting-repository-detection-strategy](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.setting-repository-detection-strategy))

    - default : 모든 public repository
    - annotation : exported==true && @RepositoryRestResource, @RestResource가 달린 자원들
    - all : annotation과 상관없이 모든 repository 노출
    - visibility : public interface만 노출

    max-page-size

             :

             :

- 지원되는 repository 종류
    - Spring Data JPA
    - Spring Data MongoDB
    - Spring Data Neo4j
    - Spring Data GemFire
    - Spring Data Cassandra

    [https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.bootstrap](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.bootstrap)

노출된 모든 엔드포인트의 링크를 갖는 홈 리소스

GET localhost:8080/api

```json
{
  "_links": {
    "orders": {
      "href": "http://localhost:8080/api/orders"
    },
    "tacoes": {
      "href": "http://localhost:8080/api/tacoes{?page,size,sort}",
      "templated": true
    },
    "ingredients": {
      "href": "http://localhost:8080/api/ingredients"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile"
    }
  }
}
```

리소스 경로와 relation name을 조정해보자

```java
@Data
@Entity
**@RestResource(rel = "tacos", path = "tacos")**
public class Taco {
		:
}
```

```json
{
  "_links": {
    "orders": {
      "href": "http://localhost:8080/api/orders"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    },
    "tacos": {
      "href": "http://localhost:8080/api/**tacos**{?page,size,sort}",
      "templated": true
    },
    "ingredients": {
      "href": "http://localhost:8080/api/ingredients"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile"
    }
  }
}
```

GET localhost:8080/api/tacos?size=5&page=1

```json
{
  "_embedded": {
    "tacos": []
  },
  "_links": {
    "first": {
      "href": "http://localhost:8080/api/tacos?page=0&size=5"
    },
    "prev": {
      "href": "http://localhost:8080/api/tacos?page=0&size=5"
    },
    "self": {
      "href": "http://localhost:8080/api/tacos?size=5&page=1"
    },
    "last": {
      "href": "http://localhost:8080/api/tacos?page=0&size=5"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile/tacos"
    }
  },
  "page": {
    "size": 5,
    "totalElements": 1,
    "totalPages": 1,
    "number": 1
  }
}
```

**페이징, 정렬 기능**

GET localhost:8080/api/tacos?size=5&page=0&sort=createdAt,desc

```json
{
  "_embedded": {
    "tacos": [
      {
        "createdAt": "2021-01-08T19:16:44.640+00:00",
        "name": "wanda-taco",
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/tacos/1"
          },
          "taco": {
            "href": "http://localhost:8080/api/tacos/1"
          },
          "ingredients": {
            "href": "http://localhost:8080/api/tacos/1/ingredients"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/tacos?size=5&page=0&sort=createdAt%2Cdesc"
    },
    "profile": {
      "href": "http://localhost:8080/api/profile/tacos"
    }
  },
  "page": {
    "size": 5,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

multiple sorting은 sort 파라미터를 계속 추가하면됨 ex)sort=createdAt,asc&sort=id,desc&sort=...

[https://docs.spring.io/spring-data/rest/docs/current/reference/html/#paging-and-sorting.sorting](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#paging-and-sorting.sorting)

### 커스텀 엔드포인트 추가하기

때로는 기본적인 CRUD API가 아닌 우리 나름의 엔드포인트를 생성해야 할 필요가 있음

스프링 데이터 REST가 자동 생성하는 엔드포인트에 우리의 커스텀 엔드포인트를 보충할 수 있음

우리의 엔드포인트는 현재..

1. spring data rest의 기본 경로로 매핑되지 않음
2. spring data rest 엔드포인트에서 반환하는 리소스의 하이퍼링크에 자동으로 포함되지 않음

```java
**@RepositoryRestController
:**
public class DesignTacoController {
    private final TacoJpaRepository tacoRepository;

    @GetMapping("/recent")
    public **ResponseEntity**<CollectionModel<TacoModel>> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        List<Taco> tacos = tacoRepository.findAll(page).getContent();

        CollectionModel<TacoModel> recentModels = new TacoModelAssembler().toCollectionModel(tacos);
        recentModels.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacos()).withRel("recents"));
        return ResponseEntity.ok(recentModels);
    }
}
```

```java
@Bean
public **RepresentationModelProcessor<PagedModel<Taco>> tacoProcessor(EntityLinks links)** {
    return model -> (PagedModel<Taco>) model.add(links.linkFor(Taco.class).slash("recent").withRel("recents"));
}
```

**@RepositoryRestController**

spring data rest 엔드포인트에 구성되는 것과 동일한 기본 경로로 매핑할 수 있음

**ResponseEntity**

@RestController와 유사해보이지만 다름

@ResponseBody를 가지고 있지 않기 때문에 직접 @ResponseBody를 명시하거나 ResponseEntity 타입을 return해야함

**RepresentationModelProcessor<PagedModel<Taco>> tacoProcessor(EntityLinks links)** 

리소스 프로세서 빈을 선언하면 spring data rest가 자동으로 포함시키는 링크 리스트에 해당 링크를 추가할 수 있음
