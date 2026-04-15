package com.example.malllearning.service;

import com.example.malllearning.common.ResultCode;
import com.example.malllearning.dto.user.RegisterRequest;
import com.example.malllearning.entity.User;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.repository.UserRepository;
import com.example.malllearning.util.Validation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名已存在");
        }

        // ✅ 使用你的 Validation 工具类
        String pwdError = Validation.validatePassword(request.getUsername(), request.getPassword());
        if (pwdError != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, pwdError);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // ✅ BCrypt 哈希
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    // login() 方法可以删除了 — 认证由 Spring Security 的 AuthenticationManager 处理

    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "用户不存在"));
        return user.getBalance();
    }
}
