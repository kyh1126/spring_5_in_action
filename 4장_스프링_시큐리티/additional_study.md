# 4장 스터디 후 질문 & 부족한 내용 추가

1. RestAPI 사용할 때 csrf 제외해줘야 하는 이유
2. {noop}왜 생겼나
3. SecurityContextHolder 
  - argument통해서 가져올 때 컨트롤러만 가져오는데, 컨텍스트 쓰레드에서 async 문제가 있을까??
  - securityContext holder 찾아보
  
4. (추가) Spring boot security 의존성 라이브러리들 역할 간략하게 정
---
## 1.RestAPI 사용 후 csrf 제외해줘야 하는 이유 

**CSRF (Cross-size Request Forgery)**
- 웹 어플리케이션 취약점 중 하나
- 사용자가 자신의 의지와는 무관하게 특정 웹 사이트에 (수정,삭제 등록등) 공격행위를 하도록 하는 것. 


대표적인 CSRF 방어 방법: 일반적으로 CSRF공격 방어는 조회성(GET) 데이터엔 방어 대상에 두지 않고, 쓰기/변경 가능한 (POST, DELETE, PUT, PATCH) 에 적용한다.
- 레퍼러 검증
- 시큐리티 토큰 사용 


레퍼러검증 : 리퀘스트의 레퍼러를 확인하여, 도메인이 일치하는지 검증하는 방법
시큐리티 토큰 활용: 사용자의 세션에 임의의 난수 값을 저장 후 해당 난수 값을 포함해 전송. 이후 백엔드에서 요청을 받을 때 마다 세션에 저장된 토큰과 요청에서 전달하는 토큰 값이 일치하는지 검증 하는 방법.


** REST API에선 csrf 보안을 disable 해야한다는데, 이유가 뭘까 ?**
- 스프링인액션(5판) 157p에, *'단, REST API 서버로 실행되는 애플리케이션의 경우는 CSRF를 disable 해야한다'* 란 설명이 있는데.. 왜일까..?
- 비슷한 의견은 스택오버플로에도 있다 (https://stackoverflow.com/questions/48985293/spring-security-stateless-rest-service-and-csrf) 

> 나름 찾은 답변
- 대부분의 csrf는 브라우저+세션 기반의 공격이 대부분임.
- 순수 REST API 호출 한다면, csrf 할 **이유가 없다**.
  - Rest API같은 경우, 브라우저 토큰 &세션을 통해 인증하는 방식을 사용하기 보다는 
  - Api Key 방식, Api Token 방식, 클라이언트 인증 방식, 제 3자 인증 방식 등의 방식을 사용하게 된다.
  - 때문에 브라우저를 통한 세션 or tricky 변환 방식을 통해 공격할 수가 없기 때문에 REST API 요청에선 csrf 사용을 disable 할 수 있다.



## 2.*{noop}왜 생겼을까**
 - {noop}은 패스워드를 인코딩 없이 처리하고 싶을 때 사용한다. 
 - {noop}이 패스워드 인코딩 없이 처리될 수 있도록 변한건, 스프링 시큐리티 5.x 이후인데, 시큐리티 도큐먼트 읽으면 잘 나옴
 - [스프링 doc에 security의 passwordEncoder 관련 설명](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#authentication-password-storage-dpe)
 
 **`DelegatingPasswordEncoder`**
 - 스프링 시큐리티 5 이전의 버전에서, default passwordEncoder는 `NoOpPasswordEncoder` 였음 ( 아무 인코딩도 하지 않는 인코더 )
 - [패스워드 히스토리 읽어보면 도움 &재미](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#authentication-password-storage-history)
   - 첨엔 plain text로 저장했음 > 안전할꺼라 생각함 > 하지만 sqlInjection 등으로 안좋구나 깨달음 > 단방향 SHA-256 사용함
   - sha256사용하니 해시값만 노출됨 > 예상하기 힘들지(파악어려움) > 이거 조회하려고 레인보우테이블이란 조회 테이블 만듦 & 암호 계산해서 저장함 > 약점
   - 레인보우 테이블 대체하려고 솔트값을 추가한 걸 패스워드에 추가함 > 해시값+솔트값 비교가 다 다르기 때문에 레인보우 테이블 방어 가능 함> 하지만 하드웨어 성능 발전하면서 이것도 역시 풀 수 있다.
   - 그래서 이제 적응형단방향(adaptive one-way function) 사용. > 이건 컴퓨터의 리소스를 사용하고, 암호화 확인하는데 시간이 좀걸림 (단위 약 1초가 걸리도록 조정이 필요함.)
   - 적응형 단방향?은 부하가 걸림(시간이 걸려서) > 성능 저하 > 암호화 속도를 높일 수 있는 방법이 필요
 
 - 이후 시큐리티에서 디폴트를 `NoOpPasswordEncoder` 에서 `BCryptPasswordEncoder`로 변경했는데, 세가지 문제가 있음
   1. 예전에 사용했 암호들이 쉽게 마이그레이션 할 수 없는 것들이 많음
   2. 암호화 최적방법이 변화됨
   3. 스프링 시큐리티가 이런 변화를 바로바로 변경할 수 없음 (프레임 워크니깐)
 - 그래서 `DelegatingPasswordEncoder`로 위의 문제를 해결하기 위함. 이걸 사용하면
   1. 현재 패스워드 저장 추천되는 방식으로 패스워드 인코딩함
   2. 현재와 레거시 포맷 모두 밸리데이션 할수 있또록 함
   3. 미래의 인코딩 방식을 사용할수 있도록 업그레이드 지정함.
 - `DelegatingPassword`는 `PasswordEncoderFactories`를 통해서 인스턴스 생성할 수 있음
   - 첨부는 따로 안하고, 독 보는게 젤 좋은듯 `PasswordEncoderFatories`의 `createDelegatingPasswordEncoder`
   - {id}로 인코더 매칭할 수 있다.
 
 - 결론적으로, {noop}password 는 {noop} NoOpPasswordEncoder를 사용하기 위한 id인 noop을 명시한 것    
 
 
 ## 3. SecurityContextHolder
 
 - 스프링 시큐리티 아키텍쳐도 함꼐 봐야 공부가 더 잘 될듯
**SecurityContextHolder**
- SecurityContext에 접근할 수 있도록 연관되어있음
- SecurityContextHolderStrategy에 따라서 스태틱 메서드들을 제공해준다. ( -> 전략에 따라, 어플리케이션 전 영역에서 접근할 수 있다고 해석할 수 있음)
  - JVM내에서 편리하게 접근할 수 있도록 함을 해당 클래스의 목적이라고 할 수 있음.
- SYSTEM_PROPERTY 로 접근하거나, setStretagyName(String) 으로 접근해서 전략을 바꿀 수 있음
  - THREADLOCAL : 기본 전략
  - MODE_INHERITABLETHREADLOCAL: 메인스레드& 자식스레드에게 동일하게 접근권한 유지 
  - MODE_INHERITABLETHREADLOCAL: 어플리케이션 전체에 하나의 시큐리티 스레드 유지
    
** @Asyc 요청과 SecurityContextHolder를 함께 쓴다면...?**
- Async호출한 요청에서, 시큐리티켄텍스트홀더에 접근해서 시큐리티 관련 인증정보를 가져오려고 하면, 문제가 되는 포인트 or 고려해야할 점이 있음
- 스프링 @Async를 예로든다면, [@Async에 대해 자세히 적어논 블로그](https://brunch.co.kr/@springboot/401) 를 참조해 공부해즈아
  - 병렬프로그래밍은 기본적으로 비동기 호출을 할 때 마다 쓰레드를 새로 생성한다.
  - 별도 설정을 하지 않는다면, 스프링부트의 ThreadPoolTaskExecutor에 의해서 실행된다.
  - (추가)리턴 값에 따라 실행 하는 방식이 다르다
  
- @Async는 별도 설정하지 않는다면 ThreadPoolTaskExecutor에 의해 실행, SecurityContextHolder는 default전략이 ThreadLocal로 서로 다른 스레드를 사용한다.
- 그래서 @Async 호출 중에 시큐리티컨텍스트홀더에 접근하면, 해당 값을 찾을 수 없다고 나오거나, 기타 에러들을 만날 수 있음
- 해결방법은 securityContextHolder의 접근 할 수 있는 전략을 변경해주면 되는데, INHERITABLETHREADLOCAL 로 설정하면 해결가능
- INHERITABLETHREADLOCAL : 스레드로컬에서 부터 생성된 하위 스레드들 까지 접근권한을 허용한다. 라고 보면됨 :)

     
    
  
  
   
 
 
 
 
 