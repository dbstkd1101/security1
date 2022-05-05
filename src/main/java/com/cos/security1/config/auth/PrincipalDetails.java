package com.cos.security1.config.auth;

// security가 "/login' 요청을 낚아채서 로그인 진행시킨다.
// 로그인 진행 완료가 되면, (security가 가지고 있는) session(Security ContextHolder)을 만들어 준다.
// session에 저장될 오브젝트 type: Authentication type 객체만
// Authentication 안에 User 정보가 있어야 됨
// User obejct type : UserDetails type 객체
// Security Session -> Authentication -> UserDetails(PrincipalDetails)
import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
// bean/component 관련 annot 없는 이유는 나중에 view에서 강제로 띄울 것이기 때문에
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 본 method의 return type 때문에 아래 getRole 사용 불가
        //user.getRole();
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        // 우리 사이트에서 1년 동안 회원이 로그인 안할 시. 휴먼 계정으로 하기로 했으면...

        return true;
    }
}
