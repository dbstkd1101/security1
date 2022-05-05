package com.cos.security1.config.jwt;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 스프링 시큐리니에서 UsernamePasswordAuthenticationFilter가 존재함.
// "/login" 요청에 의해 username, password를 POST 요청하면 해당 필터 동작
// but 현재 SecurityConfig에서 formLogin disable 한 상태 → 직접 addFilter 해야함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // "/login" 요청을 하면 로그인 시도를 위해 동작하는 method
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 1. username, password를 받아서
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 2. 정상인지 authenticationManager로 로그인 시도(=PrincipalDetailsService.loadUserByUsername() 호출)를 해봄.
            // 인증 결과 authentication에 login한 정보 담겨 있음= 로그인이 되었다는 의미 = DB에 있는 username/password가 일치
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 정상 로그인 되었는지 확인
            PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
            System.out.println("로그인 완료됨? user : "+principalDetails.getUser().getUsername());

            // 권한 관리를 security가 대신 해주므로 authentication 객체를 session에 저장해야 함(=return authentication)
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유 없음
           return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 3. PrincipalDetails를 세션에 담고(권한 관리(SecurityConfig의 antMatchers)를 위함)
        // 4. JWT 토큰을 만들어서 응답해주면 됨.
        return null;
    }

    // attempAuthentication 실행 후 인증이 정상적으로 되었으면 아래 함수가 실행됨
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
