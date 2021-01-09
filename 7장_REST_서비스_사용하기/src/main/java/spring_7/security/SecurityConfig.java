package spring_7.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/design", "/orders")
//                .hasRole("ROLE_USER")
//                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll")
                .and()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()

                .and()
                .formLogin()
                .loginPage("/login")
//                .defaultSuccessUrl("/design", true)
                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .csrf()
                .disable().headers().frameOptions().disable();
        ;

    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 커스텀 명세 서비스
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    // 인메모리 기반 사용자 스토어
    /*@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1")
                .password("{noop}password1")
                .authorities("ROLE_USER")       //.roles("ADMIN")과 동일함
                .and()
                .withUser("user2")
                .password("{noop}password2")
                .roles("USER");      //.roles("ADMIN");과 동일함
    }*/

    // JDBC 기반 사용자 스토어
    /*@Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users " +
                                "where username=?")
                .authoritiesByUsernameQuery(
                        "select username, authority from authorities " +
                                "where username=?")
                .passwordEncoder(new NoEncodingPasswordEncoder());
    }*/

    // LDAP 기반 사용자 스토어
    // https://docs.spring.io/spring-data/ldap/docs/2.4.2/reference/html/#reference
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0})")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("(member={0})")
                .contextSource()//.url("ldap://tacocloud.com:389/dc=tacocloud,dc=com")
                .url("ldap://localhost:8389/dc=tacocloud,dc=com")
//                .root("dc=tacocloud,dc=com")
//                .ldif("classpath:users.ldif")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasscode");
    }*/

}
