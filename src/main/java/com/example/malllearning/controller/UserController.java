package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.user.BalanceResponse;
import com.example.malllearning.dto.user.LoginRequest;
import com.example.malllearning.dto.user.LoginResponse;
import com.example.malllearning.dto.user.RegisterRequest;
import com.example.malllearning.entity.User;
import com.example.malllearning.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    // ────────────── 公开接口（不经过拦截器）──────────────

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ApiResponse.success(user.getId());
    }

    @Operation(summary = "用户登录", description = "登录成功后在 Session 中保存用户信息")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {

        return userService.login(request)
                .map(user -> {
                    HttpSession oldSession = httpRequest.getSession(false);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    HttpSession newSession = httpRequest.getSession(true);

                    newSession.setAttribute("userId", user.getId());
                    newSession.setAttribute("userRole", user.getRole().name());

                    LoginResponse response = new LoginResponse();
                    response.setUserId(user.getId());
                    response.setRole(user.getRole().name());
                    return ApiResponse.success(response);
                })
                .orElse(ApiResponse.fail(400, "用户名或密码错误"));
    }

    // ────────────── 需登录接口（经过拦截器）──────────────

    @Operation(summary = "用户退出")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Parameter(hidden = true) HttpServletRequest httpRequest) {
        // 拦截器已验证登录，这里只需要销毁 Session
        httpRequest.getSession().invalidate();
        return ApiResponse.success();
    }

    @Operation(summary = "查询余额")
    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> balance(
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("loginUserId");
        BigDecimal balance = userService.getBalance(userId);
        return ApiResponse.success(new BalanceResponse(balance));
    }
}
