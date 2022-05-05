package com.cos.security1.config;

import com.cos.security1.config.jwt.JwtAuthenticationFilter;
import com.cos.security1.filter.MyFilter1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity  // springSecurityFilter(SecurityConfig)가 spring filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured annot 활성화, PreAuthorize/PostAuthorize annot 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsFilter;

    @Bean   // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록(→ 어디서든(indexController) 사용할 수 있음)
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //아래와 같이 하면 SecurityFilter Chain 중 제일 먼저 시작하는 Filter보다 before 이므로, 가장먼저 실행 가능
//        http.addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class);
        //SecurityFilter Chain 中 BasicAuthenticationFilter filter가 동작하지 전에 MyFilter 실행
//        http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
//
        http.csrf().disable();
        //session을 사용하지 않는 서버가 되겠다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().addFilter((Filter) corsFilter)
        .formLogin().disable()// jwt 서버니까, 내쪽에서 ID/PW 로그인을 안함. 요청 받기만 하겠지.→ 따라서 UserDetailsService로 가는 필터 필요
        .httpBasic().disable() //기본적인 http login 방식 아예 사용 안함
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager를 param으로 던저야 함
        .authorizeRequests()
        .antMatchers("/user/**").authenticated()    //인증만 되면 들어감
        .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 인증 뿐만 아니라, 하기 권한까지 있어야 한다.
        .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll();// 위 내용 제외한 나머지 requests는 걍 허가
//                .and()
//                .formLogin()    // 403 error 대신에
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login") // login 주소 호출 시 시큐리티가 낚아 채서 대신 인증 진행해 줌.(→ controller에 /login을 안만들어도 됨). UserDetailsService.loadUserByUsername(String username)
//                .defaultSuccessUrl("/");    // 위의 loginForm을 통해서 login 시도 시에 성공하면 해당 url("/")로, 그 외의 다른 url 통해서 인증 요청시 해당 url로 자동 redirect
    }
}

