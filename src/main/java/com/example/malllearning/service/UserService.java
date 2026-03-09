package com.example.malllearning.service;

import com.example.malllearning.entity.User;
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

    public User register(String username, String password, String email) throws Exception {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if(userOpt.isPresent() && userOpt.get().getPassword().equals(password)){
            return userOpt;
        }
        return Optional.empty();
    }

    public BigDecimal getBalance(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("用户不存在"));
        return user.getBalance();
    }
}

