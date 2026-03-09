package com.example.malllearning.config;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.common.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public LoginInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userIdObj = request.getSession().getAttribute("userId");
        if (!(userIdObj instanceof Number)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            ApiResponse.fail(ResultCode.UNAUTHORIZED, "请先登录")
                    )
            );
            return false;
        }

        Long userId = ((Number) userIdObj).longValue();
        request.setAttribute("loginUserId", userId);
        return true;
    }
}
