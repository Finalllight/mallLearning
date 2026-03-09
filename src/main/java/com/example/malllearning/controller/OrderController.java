package com.example.malllearning.controller;

import com.example.malllearning.dto.order.SubmitOrderRequest;
import com.example.malllearning.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody(required = false) SubmitOrderRequest request,
                                      HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        Long userCouponId = request == null ? null : request.getUserCouponId();

        try {
            return success(orderService.submitOrder(userId, userCouponId));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping
    public Map<String, Object> list(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        return success(orderService.listOrders(userId));
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> detail(@PathVariable Long orderId, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        try {
            return success(orderService.getOrderDetail(userId, orderId));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        return null;
    }

    private Map<String, Object> success(Object data) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "success");
        resp.put("data", data);
        return resp;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "error");
        resp.put("message", message);
        return resp;
    }
}
