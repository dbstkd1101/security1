package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository anootation이 없이도 IoC(Bean등록)됩니다. 상속했으므로.
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);

}
