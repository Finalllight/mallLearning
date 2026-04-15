package com.example.malllearning.config;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.common.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public AdminInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 1. 先检查是否登录
        Object userIdObj = request.getSession().getAttribute("userId");
        if (!(userIdObj instanceof Number)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, ResultCode.UNAUTHORIZED, "请先登录");
            return false;
        }

        // 2. 检查是否是管理员
        Object roleObj = request.getSession().getAttribute("userRole");
        if (roleObj == null || !"ADMIN".equals(roleObj.toString())) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, 403, "无权限，仅管理员可操作");
            return false;
        }

        // 3. 设置用户信息到 request
        Long userId = ((Number) userIdObj).longValue();
        request.setAttribute("loginUserId", userId);
        request.setAttribute("loginUserRole", roleObj.toString());

        return true;
    }

    private void writeError(HttpServletResponse response, int httpStatus, int code, String message) throws Exception {
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(ApiResponse.fail(code, message))
        );
    }
}
