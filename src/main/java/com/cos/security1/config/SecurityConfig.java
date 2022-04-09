package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity  // springSecurityFilter(SecurityConfig)가 spring filter chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured annot 활성화, PreAuthorize/PostAuthorize annot 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean   // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록(→ 어디서든(indexController) 사용할 수 있음)
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    //인증만 되면 들어감
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 인증 뿐만 아니라, 하기 권한까지 있어야 한다.
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위 내용 제외한 나머지 requests는 걍 허가
                .and()
                .formLogin()    // 403 error 대신에
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // login 주소 호출 시 시큐리티가 낚아 채서 대신 인증 진행해 줌.(→ controller에 /login을 안만들어도 됨). UserDetailsService.loadUserByUsername(String username)
                .defaultSuccessUrl("/");    // 위의 loginForm을 통해서 login 시도 시에 성공하면 해당 url("/")로, 그 외의 다른 url 통해서 인증 요청시 해당 url로 자동 redirect
    }
}

