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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户管理", description = "用户注册、登录、退出、余额查询等接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册", description = "通过用户名、密码、邮箱注册新用户，返回用户ID")
    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ApiResponse.success(user.getId());
    }

    // UserController.login() 方法修改
    @Operation(summary = "用户登录", description = "通过用户名和密码登录，成功后在 Session 中保存用户信息")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true) HttpSession session) {
        return userService.login(request)
                .map(user -> {
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("userRole", user.getRole().name()); // ✅ 存储角色
                    LoginResponse response = new LoginResponse();
                    response.setUserId(user.getId());
                    response.setRole(user.getRole().name()); // ✅ 返回角色
                    return ApiResponse.success(response);
                })
                .orElse(ApiResponse.fail(400, "用户名或密码错误"));
    }

    @Operation(summary = "用户退出", description = "清除当前会话，退出登录")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Parameter(hidden = true) HttpSession session) {
        session.invalidate();
        return ApiResponse.success();
    }

    @Operation(summary = "查询余额", description = "查询当前登录用户的账户余额，需要先登录")
    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> balance(
            @Parameter(hidden = true) HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.fail(401, "用户未登录");
        }
        // 2. 调用Service + 直接返回（删除冗余try-catch）
        BigDecimal balance = userService.getBalance(userId);
        return ApiResponse.success(new BalanceResponse(balance));
    }
}
