package com.example.malllearning.service;

import com.example.malllearning.common.ResultCode;
import com.example.malllearning.dto.user.LoginRequest;
import com.example.malllearning.dto.user.RegisterRequest;
import com.example.malllearning.entity.User;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(RegisterRequest request)  {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail( request.getEmail());
        return userRepository.save(user);
    }

    public Optional<User> login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if(userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())){
            return userOpt;
        }
        return Optional.empty();
    }

    public BigDecimal getBalance(Long userId)  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "用户不存在"));
        return user.getBalance();
    }
}

