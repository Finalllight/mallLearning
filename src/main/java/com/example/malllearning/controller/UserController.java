package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.user.*;
import com.example.malllearning.entity.User;
import com.example.malllearning.security.LoginUser;
import com.example.malllearning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Tag(name = "用户管理", description = "用户注册、登录、退出、余额查询等接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ApiResponse.success(user.getId());
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            // 1. 使用 Spring Security 的 AuthenticationManager 进行认证
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword());

            Authentication authentication = authenticationManager.authenticate(token);

            // 2. 认证成功，将 Authentication 放入 SecurityContext
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 3. 创建新 Session 并绑定 SecurityContext（防止会话固定攻击）
            HttpSession oldSession = httpRequest.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

            // 4. 构建响应
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            LoginResponse resp = new LoginResponse();
            resp.setUserId(loginUser.getUserId());
            resp.setRole(loginUser.getUser().getRole().name());
            return ApiResponse.success(resp);

        } catch (BadCredentialsException e) {
            return ApiResponse.fail(400, "用户名或密码错误");
        }
    }

    // 退出登录已由 SecurityConfig 的 .logout() 处理，Controller 中可以删除

    @Operation(summary = "查询余额")
    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> balance(
            @AuthenticationPrincipal LoginUser loginUser) {
        BigDecimal balance = userService.getBalance(loginUser.getUserId());
        return ApiResponse.success(new BalanceResponse(balance));
    }
}
