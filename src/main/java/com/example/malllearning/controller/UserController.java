package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.user.BalanceResponse;
import com.example.malllearning.dto.user.LoginRequest;
import com.example.malllearning.dto.user.LoginResponse;
import com.example.malllearning.dto.user.RegisterRequest;
import com.example.malllearning.entity.User;
import com.example.malllearning.service.UserService;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 用户注册
    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody RegisterRequest request){
        User user = userService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
        return ApiResponse.success(user.getId());
    }

    // 用户登录
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                            HttpSession session){
        Optional<User> userOptional = userService.login(request.getUsername(), request.getPassword());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            session.setAttribute("userId", user.getId());
            LoginResponse response = new LoginResponse();
            response.setUserId(user.getId());
            return ApiResponse.success(response);
        } else {
            return ApiResponse.fail(400, "用户名或密码错误");
        }
    }


    // 用户退出
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success();
    }

    // 查看余额
    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> balance(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null){
            return ApiResponse.fail(401, "用户未登录");
        }
        try{
            BigDecimal balance = userService.getBalance(userId);
            return ApiResponse.success(new BalanceResponse(balance));
        }catch(Exception e){
            return ApiResponse.fail(500,e.getMessage() );
        }
    }
}

