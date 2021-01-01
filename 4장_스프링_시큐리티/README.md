#챕터 4 스프링 시큐리티

- 스프링 시큐리티 자동구성하기
- 커스텀 사용자 스토리지 정의하기
- 커스텀 로그인 페이지 만들기
- CSRF 공격으로부터 방어하기
- 사용자 파악하기

## 1. 스프링 시큐리티 활성화 하기
- 최초에 스프링 부트 시큐리티 라이브러리를 추가해줌
- spring-boot-starter-security 와 springframewor.security 패키지의 spring-security-test
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
    </dependency>
    ```
 
 > 시큐리티 해당 라이브러리 추가 했을 때 추가되는 내용 확인해보기

시큐리티 라이브러리 추가하면서 추가되는 & 변경되는 사항
- 시큐리티에서 기본 제공하는 HTTP 로그인 페이지가 추가됨 (/login)
- 콘솔에 패스워드 입력 노출 > 좋은 방법 아니
- 모든 http 경로는 인증되어야 한다 ( 스프링 시큐리티 HTTP 기본인증 사용 )
- 역할 & 권한이 생성되지 않음
- default 사용자 추가됨 ( user ), 비밀번호는 암호화됨 (콘솔 출력)

라이브러리를 추가하면서 기본적으로 변경되는 부분이 많다. 하지만 변경부분이 서비스에 사용하기엔 부족하므로, 변경되는 부분이 어디인지, 어디서 해당 기능이 추가된 것인지 확인 할 필요가 있음.
- WebSecurityConfigurerAdapter 또는 WebSecurityConfigurer
  - WebSecurityConfigurerAdapter를 상속받거나, WebSecurityConfigurer를 구현해서 사용하기 ( 전자 선호 )
  - @EnableWebSecurity 어노테이션을 선언해줬을때 스프링 컨피규레이션이 임포티드 된다.
  - @EnableWebSecurity 를 선언하면, configure 메서드들이 오버라이드 되어서, 추상클래스 구현하는것. 그리고 기본설정값도 나와있다.
```/**
 * Uses a {@link WebSecurity} to create the {@link FilterChainProxy} that performs the web
 * based security for Spring Security. It then exports the necessary beans. Customizations
 * can be made to {@link WebSecurity} by extending {@link WebSecurityConfigurerAdapter}
 * and exposing it as a {@link Configuration} or implementing
 * {@link WebSecurityConfigurer} and exposing it as a {@link Configuration}. This
 * configuration is imported when using {@link EnableWebSecurity}.
 *
 * @see EnableWebSecurity
 * @see WebSecurity
 * @author Rob Winch
 * @author Keesun Baik
 * @since 3.2
 */
```

- 스프링 시큐리티가 제공하는 필터들
- 

## 2. 스프링 시큐리티 구성하기
예제에 나오는 코드 구현
- @Configuration, @EnableWebSecurity를 선언한 스프링 시큐리티 컨피그 클래스 구현
- WebSecurityConfigurerAdapter를 구현한 클래스 생성하기
- configure(HttpSecurity http)와 configure(AuthenticationManagerBuilder auth)를 오버라이딩 받아, http 요청에 관한 보안과 권한부여를 선언함
추가
- {noop} 의미
- antMatchers
  - >Part of this mapping code has been kindly borrowed from Apache Ant
  - 앤트 스타일 매칭 의미 : ? , *, **
    - ? : 1개의 이상의 문자와 매칭
    - * : 0개 이상의 문자와 매칭 ('' 부터 ~ 디렉토리 이전 파일명)
    - ** : 0개 이상의 디렉토리와 파일 매칭 ('' 부터 / 포함 한 모든 경)

## 스프링 시큐리티에 인증정보를 저장하는 방법
- AuthenticationManagerBuilder API를 사용해 빌더 형식으로 인증정보를 사용
- 메모리, JDBC, LDAP 방식으로 구현하는 인터페이스를 제공함 `inMemoryAuthentication()`,`jdbcAuthentication()`,`ldapAuthentication()`
- 스프링5부터 비밀번호는 반드시 암호화 되어야 하므로, 암호화 하지 않으면 403or 500에러 발생함.


### 1. 인메모리 사용자 구현
- 메모리에 저장
- 어플리케이션이 종료되면, 권한도 삭제됨

### 2. JDBC 사용자 구현
- `jdbcAuthentication()` 호출을 통해 구현. 이때 데이터 소스도 함꼐 설정해야함.
- jdbc 템플릿 사용시 사용자 정보를 저장하는 테이블&쿼리가 디폴트로 설정됨 ( JdbcUserDetailsManager.java 클래스에 작성되어 있다)
  - 사용자 인증 쿼리 : `select username,password,enabled from users where username = ?`
  - 사용자 권한 쿼리 : 
  - 사용자 그룹&그룹권한 쿼리 :
- 시큐리티가 제공하는 데이터베이스와 다른 데이터베이스를 사용하려면, 커스텀 하게 부여 할 수 있음
  - (JdbcUserDetailsManagerConfigurer)의 값을 변경해준다
- 하지만 우리 예제 db엔 해당 테이블 정보가 없으니까 테이블 생성하고 사용자 추가해줘야함 -> schema.sql 사용

*암호화 된 비밀번호 사용하기*
- 스프링 5 이후 스프링 시큐리티 사용시 passwordEncoder를 필수로 매핑해줘야 한다
`java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"`

패스워드 인코더 예제 ( 좋은 블로그 https://gompangs.github.io/2019/02/27/PasswordEncoder/)
- BCryptPasswordEncoder : bcrypt를 해싱 암호화
  > salt..?
  > A salt is usually a user-specific value which is added to the password before the digest is computed.
  > If a per-user salt is used (rather than a system-wide salt), it also means users with the same password will have different digest encoded passwords in the backend store.  

- NoOpPasswordEncoder
   - 암호화 하지 않음 -> 테스트 목적의 인코더, deprecated됨. 하위버전 호환을 위해서만 남겨져 있음
   - 사용하려면 생성자 생성 아니고, 싱글톤으로 static 객체 받아옴
- Pbkdf2PasswordEncoder : PBKDF2 암호화
- SCryptPasswordEncoder : scrypt 해싱 암호화
- StandardPasswordEncoder : SHA-256 암호화 ( Deprecated)

- 커스텀 인코더를 생성해서 매핑할 수도 있다 (PasswordEncoder 를 구현한 클래스 생성)


### 3. LDAP 사용자 구현
- LDAP:  Lightweight Directory Access Protocol (디렉터리 기반 조회&수정 할 수 있는 프로토콜)
- 동일 인증정보를 다른 서비스에서 가져가야 할  인증정보가 담긴 LDAP 서버를 앞에 두고 LDAP서버를 통해 인증정보를 가져갈 수 있도록 제어하는 경
- inMemory, JDBC가 사용했던 UserDetailManager를 사용하지 않는다.
- ldapAuthentication() 메서드 사용해 엘답 기반 생성
```java
// ldap 인증의 간단 구성
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication()
				.userSearchFilter("(uid={0})") // ldap 기본 쿼리의 필터를 제공하기 위해 사용됨.
				.groupSearchFilter("(member={0})");
}
```

- xxxSearchBase() : 사용자를 찾기 위한 기준점을 제공함. ex) userSearchBase(), groupSearchBase()
```java
// ldap 기준점을 지정해 계층 변경을 할 수 있음
		auth.ldapAuthentication()
				.userSearchBase("ou=people")  // ou = Organizational Unit
				.userSearchFilter("(uid={0})")
				.groupSearchBase("ou=groups")
				.groupSearchFilter("(member={0})");

```

*비밀번호 비교 구성하기*
- 입력된 비밀번호를 ldap 디렉터리에 전송 후, 사용자의 속성값과 비교하도록 ldap 서버에 요청함
- 비밀번호 비교 방법으로 ldap 인증을 구성하려면 passwordCompare() 메서드를 추가해줌
```java
		auth.ldapAuthentication()
				.userSearchBase("ou=people")  // ou = Organizational Unit
				.userSearchFilter("(uid={0})")
				.groupSearchBase("ou=groups")
				.groupSearchFilter("(member={0})")
                .passwordCompare()  // ldap 서버에 있는 userPassword속성과 비교함.
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasswcode")  //폼에서 전송되는 password의 속성명이 userPassword가 아니면 이름을 지정해서 사

```

*LDAP 서버 구성하기*
- LdapAuthenticationProviderConfigurer 내부 클래스인 ContextSourceBuilder 객체의 메서드 사용
- 외부 = url()메서드 사용, 내장ldap = root() 메서드 사용함
- 내부 ldap 사용 시 추가해줘야 할 의존성들 존재
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-ldap</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.ldap</groupId>
            <artifactId>spring-ldap-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-ldap</artifactId>
        </dependency>
```
- 책 예제가 5.1.4, 현재 내 스프링시큐리티가 5.4.2 인데, 내부 구현이 달라져서 ldap 서버 의존성을 추가해 줘야함
```xml
        <dependency>
            <groupId>com.unboundid</groupId>
            <artifactId>unboundid-ldapsdk</artifactId>
        </dependency>
```
  - unboundid 톰캣 사용할 때, 계속 오류가 발생해서 일단 url세팅을 설정 변경해 적용함. > 여러분의 도움 필요
  - + 로그인이 안된다..
  
  
*사용자 인증 커스터마이징*
- JDBC 기반 인증을 사용하는 사용자 인증 커스터마이징 객체 생성&사
- SpringSecurity의 UserDetails를 상속받는 객체 생성
```java
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private final String username;
	private final String password;
	private final String fullname;
	private final String street;
	private final String city;
	private final String state;
	private final String phoneNumber;

    // 시큐리티의 UserDetails 인터페이스에 있는 메서드 들을 오버라이드 해서 정의한다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
```
- UserRepository를 생성해, 이름기반으로 조회할 수 있또록 설정.

- 커스텀 서비스를 시큐리티 구성에 추가하기
- configure 메서드 내부에서 userDetailService를 사용할 수 있도록 전달.
```java
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder encoder(){ // passwordEncoder를 스프링컨텍스트에 빈으로 등록해서 주입받도록 사용
		return new BCryptPasswordEncoder();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(encoder());
	}
```

*사용자 등록하기*
- RegistrationController 로 사용자 등록 폼 요청 받는 컨트롤러 생성
- RegistrationForm.java 클래스로  사용자 요청을 받고, db에 조회 시 passwordEncode를 거쳐 user 접근하도록 설정
```java
public class RegistrationForm {

	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String phoneNumber;

	public User toUser(PasswordEncoder passwordEncoder){
		return new User(username,passwordEncoder.encode(password),fullname,street,city,state,phoneNumber);
	}
}
```

## 3 웹 요청 보안 처리하기
- 인증되야 접근할 수 있는 페이지(보안), 인증되지 않아도 모든 사용자가 볼 ㅅ ㅜ있는 페이지 설정
- SecurityConfigurer 클래스 중 configure(HttpSecurity http)에서 보안 요청 설정
- antMachers(), hasRole()등 정의된 메서드를 이용하여 요청 별 권한 부여.
  - 요청별 권한 부여 정의한 메서드 
    | 메서드 | 작업 |
    | access(String) | SpEL표현식이 true면 허용 |
    | annonymous() | 익명 사용자에게 접근 허용 |
    | authenticated() | 인증된 사용자인 경우 접근 허용 |
    | fullAuthenticated() | 익명& remember-me 인 사용자는 접근 거부 (그외 접근 허용) |
    | hasAnyAuthority(String ... ) | 지정된 권한 중 하나라도 있으면 접근 허용 |
    | hasAnyRole(String...) | 지정된 역할중 어느 하나라도 갖고 있으면 접근 허용 |
    | hasAuthority(String) | 지정된 권한 있는 경우 허용 |
    | hasIpAddress(String) | 지정된 ip로부터 요청이 오면 접근 허용 |
    | hasRole(String) | 지정된 역할 접근 허용 |
    | not() | 다른 접근 메서드 무효화 |
    | permitAll() | 무조건 접근 허용 |
    | denyAll() | 무조건 접근 거부 |
    | rememberMe() | 이전 접근 정보를 통해 인증된 사용자 접근 허용 |
    
  - 스프링 시큐리티에서 확장된 SpEL(access()에 파라미터로 전달)
    | SpEL 표현식 | 결과 |
    |authentication | 해당 사용자의 인증 객체 |
    |principal | principal 객체 |
    |denyAll | 항상 false |
    |permittAll | 항상 true |
    |hasAnyRole() | 지정한 역할 목록 중 아무거나 갖고 있음 true |
    |hasRole() | 지정한 역할이면 true |
    |hasIpAddress() | 동일 아이피면 true |
    |isAnonymus() | 익명 사용자면 true |
    |isAuthenticated() | 인증된 사용자면 true |
    |isRememberMe() | remember-me로 인증 되었으면 true |
    |isFullyAuthenticated() | 익명&rememberMe가 아니면 true |

- SpEL로 생성 시, 규칙을 자유롭게 확장할 수 있어서 조금더 유연.
- 이때 ~*순서가 중요함*~ 

## 3.2 커스텀 로그인 페이지 생성하기
```java
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            .antMatchers("/design", "/orders")
            .access("hasRole('ROLE_USER')")
        .antMatchers("/", "/**")
            .access("permitAll")
        //로그인 설정
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/design",true)
        //로그아웃 설정
        .and()
        .logout()
        .logoutSuccessUrl("/")
        .and()
        .csrf();
	}
```

- 시큐리티가 기본으로 제공하는 로그인 페이지 외에 사용자 작성한 로그인 페이지로 연결하기.
- httpSecurity의 formLogion()을 호출하여 사용
- httpSecurity의 and()어펜더를 사용해서 추가적으로 경로를 지정할 수 있다.
- 시큐리티에서는 기본적으로 /login 경로로 로그인 요청을 간주함, 하지만 변경하고 싶으면 .loginProcessingUrl()메서드를 통해 변경할 수 있음
  - `.loginProcessingUrl()`: 로그인 요청 url 설정
  - `.usernameParameter()` : 사용자 이름 파라미터명 설정 
  - `.passwordParameter()` : 사용자 패스워드 파라미터 명 설정
  - `defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysUse)`: 로그인 후 / 경로로 이동하는데, 로그인 경로를 변경할 수 있음. alwaysUse는 이전페이지가 어디었던지 간에 무관하게 로그인 성공하면 이동하도록 변경함.

### 4. CSRF 공격 방어하기

- Cross-Site Request Forgery: 웹상에서 스크립트 위조 요청
- CSRF 토큰을 요청에 포함시켜 방어 
- 시큐리티내에 기본으로 활성화 되 있음.
- http요청에 .csrf()로 csrf configure를 설정할 수 있음
  - disable(), ignoreAntMatchers, csrfTokenRepository() ...등
- h2 콘솔이 csrf가 안먹기 때문에 제외해줘야 한다 !


## 4 사용자 인지하기
- user 로그인을 알게되면, 더 나은 ux를 위해서 제공해줄 수 있음
  - ex) 주문 생성할 때, 사용자의 주소 등 정보를 폼에 미리 넣으면 편하다
- 사용자 누구인지 알 수 있는 방법들
  - Principal 객체를 컨트롤러에 주입
    - principal : 컴퓨터가 인증될 수 있는 엔티티들
  - Authentication 객체 주입
  - SecurityContextHolder 사용
  - @AuthenticationPrincipal 에너테이션 지정
  
```java 
//참고 코드1 - Principal
@PostMapping
public String processOrder(@Valid Order order ,Errors errors,
                            SessionStatus sessionStatus,
                            Principal principal) {

    User user = userRepository.findByUsername(principal.getName());
    order.setUser(user); 
}
```
```java 
//참고 코드2 - Authentication
@PostMapping 
public String processOrder(@Valid Order order ,Errors errors,
                            SessionStatus sessionStatus,
                            Authentication authentication) {

    User user = (User)authentication.getPrincipal(); // Object Type을 반환해서 캐스팅 필요
    order.setUser(user); 
}
```
```java 
//참고 코드3 - @AuthenticaionPrincipal
@PostMapping
public String processOrder(@Valid Order order ,Errors errors,
                            SessionStatus sessionStatus,
                            @AuthenticationPrincipal User user) {

    order.setUser(user); 
}
```
```java 
//참고 코드4 - 컨텍스트로 부터 Authentication 객체 사용
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal()

```


