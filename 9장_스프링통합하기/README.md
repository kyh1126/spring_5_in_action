# 9. 스프링 통합하기
스프링 통합(Spring integration)으로 통합 패턴을 사용하기

## 1. 통합 플로우 선언하기 (Integration flow)
채널 어댑터
- ex. 파일시스템에 데이터를 쓰는 통합 플로우 생성.
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-integration</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.integration</groupId>
            <artifactId>spring-integration-file</artifactId>
        </dependency>
```

- spring-boot-starter-integration : 반드시 추가해줘야 하는 디펜던시
- spring-integration-file : 통합 파일의 엔드포인트 모듈 중 하나. (책에선 24개 이상의 엔드포인트 모듈이 있다고 )함
- 위의 작업 이후 게이트워이 생성해줘야

**게이트웨이 인터페이스 작성& 코드 분석**
- `@MessagingGateway`
  - defaultRequestChannel
- `@Header`

**스프링 통합 플로우 구성 -> 추가로 작성**
- XML 구성
- 자바구성
- DSL 사용한 자바 구성

---
~**XML구성으로 작성하기**~
```xml
<!-- 채널을 선언함 - 쓰기 채널-->
<int:channel id="textInChannel"/>

<!-- 변환기.
     선언한 채널로 들어온 것은 toUpperCase로 변환 후, fileWriterChannel로 아웃풋 -->
<int:transformer id="upperCase"
    input-channel="textChannel"
    output-channel="fileWriterChannel"
    expression="payload.toUpperCase()" />

<!-- filwWriterChannel을 선언 
    변환기와 아웃바운드 채널 어댑터를 연결하는 전달자 역할-->
<int:channel id="fileWriteChannel" />

<!-- 파일모듈에 쓰기를 제동하는 채널. 
    fileWriteChannel을 사용해서, 해당 디렉터리에 파일을 쓴다. -->
<int-file:outbound-channel-adapter id="writer"
    channel="fileWriteChannel"
    directory="/temp/sia5/files"
    mode="APPEND"
    append-new-line="true" />
```
위의 선언한 fileWriterGateway를 사용하기 위해선 @ImportResource 어노테이션을 선언해 import 해줘야한다.

~**Java로 작성하기**~
- 위의 xml에 선언했던 것들을 자바코드로 빈 생성 해주면 됨.

~**DSL로 작성하기**~
- DSL (Domain Specific Language)
- 각 컴포넌트를 별도 빈으로 생성하지 않고, 전체 플로를 아우를 수 있는 하나의 빈으로 선언한다. -> 하나의 빈으로 간결하게 작성할 수 있음.

## 2. 스프링 통합 컴포넌트 살펴보기
스프링 통합 프로우 : 하나 이상의 컴포넌트로 구성됨.
컴포넌트들의 역할
- 채널 (channel) : 한 요소로부터 다른 요소로 메세지 전달
- 필터 : 조건에 맞는 메세지가 플로우를 통과하게 해줌
- 변환기 (Transformer) : 메세지 값을 변경하거나 메세지 페이로드의 타입을 다른 타입으로 변환한다.
- 라우터 (Router) : 여러 채널중 하나로 메세지를 전달. 보통 헤더기반으로 함
- 분배기 (Splitter) : 들어오는 메세지를 두개 이상의 메세지로 분할. 분할한 메세지는 각 채널로 전송됨
- 집적기 (Aggregator) : 분배기와 상반된 것으로, 별개 채널로부터 전달되는 다수의 메세지를 하나로 결합함(통합하는거라고 봄 N개의 메세지 -> 1개로 통합)
- 서비스 액티베이터 : 메세지를 처리하도록 자바 메서드에 메세지를 넘겨준 후 메서드의 반환값을 출력 채널로 전송함
- 채널 어댑터(adapter) : 외부 시스템에 채널을 연결. 외부시스템으로 부터 입력을 받거나 쓸 수 있음.
- 게이트웨이: 인터페이스를 통해 통합 플로우를 데이터로 전달함.

> 통합 플로우의 컴포넌트 알아보기
### 2.1 메세지 채널
- 통합 파이프라인을 통해서 메세지가 이동하는 수단.
- org.springframework.integration.channel 패키지.
- AbstractMessageChannel이 기본 구현 클래스의 베이스가 됨.
- 다양한 구현체들
  - PublicshSubscribeChannel : 하나이상의 컨슈머로 전달됨. 컨슈머가 여럿일 떈 모든 컨슈머가 메세지를 수신함
  - QueueChannel : FIFO 방식으로 컨슈머가 가저갈 때 까지 큐에 저장됨. 여러 컨슈머 중 하나의 컨슈머만 메세지를 수신한다
  - PriorityChannel: QueueChannel과 유사하지만 우선순위 헤더를 기반으로 메세지를 가져감
  - RendezvousChannel: queue와 비슷하지만, 내부적으로 SynchronousQueue로 되어있음 컨슈머가 메세지에 도착할 때 까지 메세지 전송자가 채널을 차단함( 동기화 )
  - DirectChannel : sender와 동일한 스레드로 실행되는 컨슈머에게 메세지를 전송함. (channel that invokes a single subscriber for each sent Message.) (default)
  - ExecutorChannel: DirectChannel과 유사하지만, 트랜잭션 컨텍스트를 공유하지 않음
  - FluxMessageChannel: 프로젝트리액트의 플럭스 기반인 리액트 스트림즈 퍼블리셔.
- 기본 구현체는 DirectChannel이고, 다른 메세지 채널 구현체를 사용하려면, 빈 생성 후 사용하는 부분에 채널을 명시해줘야한다
    ```java
    //빈 선언
    @Bean
    public MessageChannel orderChannel(){
        return new PublishSubscribeChannel()
    }
    //...
    
    //사용
    @ServiceActivator(inputChannel="orderChannel")
    ```
- `QueueChannel`을 사용할 때는 컨슈머가 채널을 polling 하도록 구성해야함.
    ```java
    //queueChannel 선언
    @Bean
    public MessageChannel orderChannel(){
        return new QueueChannel();
    }
    // orderChannel이란 이름의 채널에 1초마다 메세지가 있는지 확인한다. 
    @ServiceActivator(inputChannel="orderChannel", poller=@Poller(fixedRate="1000"))
    ```
  
### 2.2 필터
- integration 과정 중 중간에 위치함. 플로우 전단계에서 다음단계로 메세지 전달 허용한다
- org.springframework.integration.filter
- ex. 정수값 채널 numberChannel, 짝수인 경우는 evenNumberChannel
```java
@Filter(inputChannel="numberChannel", outputChannel="evenNumberChannel")
public boolean evenNumberFilter(Integer number) {
    return number % 2 == 0;
}
```
```java
//DSL 사용하기
@Bean
public IntegrationFlow evenNumberFlow(AtomicInteger integerSource) {
    return IntegrationFlow
        ...
        .<Integer>filter((p) -> p % 2 == 0)
        ...
        .get();
}   
```

### 2.3 변환기
- 메세지 값의 변경이나 타입을 변환하는 일을 수행함.
- Transformer인터페이스의 transform 을 상속받아 사용, 구현체 짱 많음 
- Stream으로 치면. map같은 것일까..?
- @Transformer 어노테이션으로 선언할 수 있음.
```java
@Bean
@Transformer(inputChannel="numberChannel", outputChannel="romanNumberChannel)
public GenericTransformer<Integer, String> romanNumTransformer() {
    return RomanNumbers::toRoman;
}
```
- @Transformer 어노테이션은 해당 빈을 변환기(Transformer)로 지정함.
  - 위의 코드는 numberChannel이란 채널로 integer 수신, string으로 변환 후 romanNumberChannel 이란 채널로 전송함.
- dsl 방식으론 toRoman() 메서드로 사용할 수 있음.

### 2.4 라우터
- 조건을 기반으로 플로우 내부 분기
- ex 정수값 numberChannel, 짝수메세지는 evenChannel로 전달, 홀수는 oddChannel로 전달하도록 하는 라우터를 생성
```java
@Bean
@Router(inputChannel="numberChannel")
public AbstractMessageRouter evenOddRouter() {
    return new AbstractMessageRouter() {
        @Override
        protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
            Integer number = (Integer)message.getPayload();
            if (number % 2 == 0) {
                return Collections.singleton(evenChannel());
            }
            return Collections.singleton(oddChannel());
        }
    };
}

@Bean
public MessageChannel evenChannel() {
    return new DirectChannel();
}

@Bean
public MessageChannel oddChannel() {
    return new DirectChannel();
}
```
```java
//dsl 구성

@Bean
public IntegrationFlow numberRoutingFlow(AtomicInteger source) {
    return IntegrationFlows
        ...
        .<Integer, String>route(n -> n % 2==0 ? "even":"odd", mapping -> mapping.sub)
}
```

### 2.5 분배기(splitter)
- 메세지가 별도의 하위 플로우로 처리할 수 있게 두개 이상으로 분리함.
- 분배기를 사용할 수 있는 두가지 중요 경우
  - 메세지 페이로드가 같은 타입의 컬렉션 항목들을 포함하여, 각 메세지 페이로드 별로 처리하고자 할 때
  - 연관된 정보를 함께 전달하는 하나의 메세지 페이로드는 두개 이상의 서로 다른 타입 메세지로 분할될 수 있다.
```java
    //ex. 주문데이터가 메세지 대금 정보와, 주문항목 정보로 나누기
    
    public class OrderSplitter {
        public Collection<Object> splitterOrderIntoParts(PurchaseOrder po) {
            ArrayList<Object> parts = new ArrayList<>();
            parts.add(po.getBillingInfo());
            parts.add(po.getLineItems());
            return parts;
        }
    }

    // poChannel 채널로 부터 메세지가 들어오고, 분배기인 orderSplitter가 메세지를 분해.
    // 결과는 splitterOrderChannel로 전달함.
    // 결과의 서브flow에 라우터를 선언하여 하위 플로우로 전달할 수 있음. 
    @Bean
    @Spliter(inputChannel="poChannel", outputChannel="splitterOrderChannel")
    public OrderSplitter orderSplitter() {
        return new OrderSplitter();
    }
```
```java
@Bean
@Router(inputChannel = "splitterOrderChannel")
public MessageRouter splitOrderRouter() {
    PayloadTypeRouter router = new PayloadTypeRouter();
    router.setChannelMapping(
        BillingInfo.class.getName(), "billingInforChannel")
    router.setChannelMapping(
        List.class.getName(), "lineItemsChannel" );
    return router;   
}
```

### 2.6 서비스 액티베이터
- 입력 채널로부터 메세지를 수신하고, 이 메세지를 MessageHandler 인터페이스를 구현한 클래스에 전달한다.
```java
// 서비스 액티베이터로 구성된 messageHandler 빈을 선언하는 방법
@Bean
@ServiceActivator(inputChannel="someChannel")
public MessageHandler sysoutHandler(){
    return message -> {
        System.out.println("Message payload:" + message.getPayload());
    }
}
```

```java
// 바다은 메세지의 데이터를 처리 후 새로운 페이로드 반환하는 서비스 액티베이터
@Bean
@ServiceActivator(inputChannel="orderChannel", outputChannel="completeChannel")
public GenenricHandler<Order> orderHandle(OrderRepository orderRepository) {
    return (payload,headers) -> {
        return orderRepository.save(payload);
    }
}
```

```java
	//dsl로 구성하기
	public IntegrationFlow someFlow() {
		return IntegrationFlows
					.from("orderChannel")
				.handle(System.out::println)
				.get();
	}
	//서비스 액티베이터를 가장 마지막에 두지 않고, 중간에 사용하면 
	//MessageHandler 말고 GenericHandler 사용해서 페이로드를 전달
	public IntegrationFlow orderFlow() {
		return IntegrationFlows
					.from("orderChannel")
				.<Order>handle(((payload, headers) -> new Order(payload.id,payload.items)))
				.get();
	}
```

### 2.7 게이트웨이
- 어플리케이션이 스프링 통합 플로우로 데이터를 전송하고, 처리응답결과를 받을 수 있는 수단.
- 게이트웨이는 springIntegration으로 메세지 전송할 수 있는 인터페이스로 구체화 되어 있음.
- 게이트웨이는 단방향(응답값을 받지않음 . void, ex FileWriterGateway), 양방향( 스프링인테그레이션으로 요청 전송 하고, 반환 받는다.)
```java
//ex. 양방향 게이트웨이. 인터페이스로 게이트웨이라고만 명시해주면, 구현하지 않아도 된다 
@Component
@MessagingGateway(defaultRequestChannel = "inChannel", defaultReplyChannel = "outChannel")
public interface BidirectGateway {
	String uppercase(String in);
}
```

### 2.8 채널 어댑터
- 통합 플로우의 입구와 출구
- inbound 어댑터로 데이터가 들어와 통합플로우로, outbound 어댑터로 통합 플로우에서 나감
- DSL 플로우에서는 from 메서드가 인바운드 어댑터를 수행함.
- 아웃바운드 채널 어댑터는 통합 플로우의 끝단. 최종 메세지를 어플리케이션이나 다른 시스템에 넘겨준다


### 2.9 엔드포인트 모듈
- 다양한 외부 시스템과통합을 위해 채널어댑터가 포함된 24개의 엔드포인트 모듈이 제공됨 
- AMQP, 스프링 어플리케이션 이벤트, RSS & Atom, 파일시스템, FTP, GemFire, HTTP, JDBC....

## 3. 이메일 통합 플로우 생성