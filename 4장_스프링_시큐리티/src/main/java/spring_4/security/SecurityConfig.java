package spring_4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder encoder(){

//		return new BCryptPasswordEncoder();
		return new NoEncodingPasswordEncoder();
	}

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
//		.defaultSuccessUrl("/design",true)
		//로그아웃 설정
		.and()
		.logout()
		.logoutSuccessUrl("/")
		.and()
		.csrf()
				.disable()
//		.and()
		.headers().frameOptions().disable();
	}

	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(encoder());
	}

		// inMemory way
//		auth.inMemoryAuthentication()
//				.withUser("user1")
//				.password("{noop}password1")
//				.authorities("ROLE_USER")
//				.and()
//				.withUser("user2")
//				.password("{noop}password2")
//				.authorities("ROLE_USER");

		//jdbc
/*
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				//JdbcUserDetailsManagerConfigurer의 설정값 변경해줌
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
				//암호화 알고리즘 사용 - 스프링 5 이후 시큐리티 사용할 땐 필수다
//
//				.passwordEncoder(new BCryptPasswordEncoder()); // BCryptPasswordEncode
//				.passwordEncoder(NoOpPasswordEncoder.getInstance());  // NoOpPasswordEncoder - deprecated
				.passwordEncoder(new NoEncodingPasswordEncoder());  // custom password encoder
*/

		//ldap
/*
		auth.ldapAuthentication()
				.userSearchBase("ou=people")
				.userSearchFilter("(uid={0})")
				.groupSearchBase("ou=groups")
				.groupSearchFilter("(member={0})")
				.contextSource()
				.url("ldap://localhost:8389")
				.root("dc=tacocloud,dc=com")
				.ldif("classpath:users.ldif")
				.and()
				.passwordCompare()
//				.passwordEncoder(new BCryptPasswordEncoder())
				.passwordEncoder(new NoEncodingPasswordEncoder())
				.passwordAttribute("userPasscode");
	}
*/
}
