package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view를 return 하겠다, 본 spring boot의 template engine으로 jsp가 아닌 mustache가 설치됨
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"", "/"})
    public String index(){
        //머스테치(Spring이 권장). default path : src/main/resources/
        // 뷰리졸버 설정 : templates를 prefix로 잡고, ".mustache"는 suffix로 잡으면 끝남
        return "index"; //src/main/resources/templates/index.mustache
        //우리는 index.html을 만들었으므로 viewResolver 등록 필요(→ WebMvcConfig.java 참조)
    }

    @GetMapping("/user")
    public @ResponseBody  String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // springSecurity가 해당 주소 낚아 챔 → SecurityConfig 에서 설정 후 작동 안함.
    //@GetMapping("/login")
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user)
    {
        user.setRole("ROLE_USER");
        //userRepository.save(user); // 회원가입은 잘되나, 비밀번호가 "1234"로 들어가는 문제
        //패스워드 암호화가 안되어서 시큐리티로 로그인 불가.

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")//본 method 실행 직전에 실행됨. 여러개 걸고 싶을 때
    //@PostAuthorize
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}
