package com.example.malllearning.controller;

import com.example.malllearning.entity.User;
import com.example.malllearning.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 用户注册
    @PostMapping("/register")
    public Map<String,Object> register(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam(required = false) String email){
        Map<String,Object> response = new HashMap<>();
        try{
            User user = userService.register(username,password,email);
            response.put("status","success");
            response.put("userId",user.getId());
        }catch(Exception e){
            response.put("status","error");
            response.put("message","注册失败，请稍后重试");
        }
        return response;
    }

    // 用户登录
    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam String username,
                                    @RequestParam String password,
                                    HttpSession session){
        Map<String,Object> response = new HashMap<>();
        userService.login(username,password).ifPresentOrElse(
                user -> {
                    // 将用户ID存入 Session
                    session.setAttribute("userId",user.getId());
                    response.put("status","success");
                    response.put("userId",user.getId());
                },
                () -> {
                    response.put("status","error");
                    response.put("message","用户名或密码错误");
                }
        );
        return response;
    }

    // 用户退出
    @PostMapping("/logout")
    public Map<String,Object> logout(HttpSession session){
        session.invalidate();
        Map<String,Object> response = new HashMap<>();
        response.put("status","success");
        return response;
    }

    // 查看余额
    @GetMapping("/balance")
    public Map<String,Object> balance(HttpSession session){
        Map<String,Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null){
            response.put("status","error");
            response.put("message","请先登录");
            return response;
        }
        try{
            BigDecimal balance = userService.getBalance(userId);
            response.put("status","success");
            response.put("balance",balance);
        }catch(Exception e){
            response.put("status","error");
            response.put("message",e.getMessage());
        }
        return response;
    }
}

