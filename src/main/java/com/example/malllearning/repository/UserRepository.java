package com.example.malllearning.repository;

import com.example.malllearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查询用户，用于登录和注册检查
    Optional<User> findByUsername(String username);
}