package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// SecurityConfig에서 loginProcessingUrl("/login");
// 해당 /login 요청이 오면 자동으로 UserDetailsService type으로 IoC 되어있는 객체의 loadUserByUsername 실행(규칙)

@Service
public class PrincipalDetailsService implements UserDetailsService {
    // 만약 loginForm의 form에서 input 태그의 name 속성의 값을 "username"으로 안하면 동작 안함
    @Autowired
    private UserRepository userRepository;

    // Security Session -> Authentication -> UserDetails(PrincipalDetails)
    // Security Session -> Authentication(내부에 UserDetails)
    // Security Session(내부 Authentication(내부에 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
