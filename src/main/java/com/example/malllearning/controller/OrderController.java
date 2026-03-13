package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.order.SubmitOrderRequest;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.service.OrderService;
import com.example.malllearning.vo.OrderVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public ApiResponse<OrderVO> submit(@RequestBody(required = false) SubmitOrderRequest request,
                                       HttpSession session) {
        Long userId = requireLogin(session);
        Long userCouponId = request == null ? null : request.getUserCouponId();
        return ApiResponse.success(orderService.submitOrder(userId, userCouponId));
    }

    @GetMapping
    public ApiResponse<List<OrderVO>> listOrders(HttpSession session) {
        Long userId = requireLogin(session);
        return ApiResponse.success(orderService.listOrders(userId));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderVO>detail(@PathVariable Long orderId, HttpSession session) {

        Long userId = requireLogin(session);
        return ApiResponse.success(orderService.getOrderDetail(userId, orderId));

    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        return null;
    }

    private Long requireLogin(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}
