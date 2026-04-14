package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.order.SubmitOrderRequest;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.service.OrderService;
import com.example.malllearning.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理", description = "提交订单、订单列表、订单详情等接口")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "提交订单", description = "将购物车中的商品提交为订单，可选使用优惠券。请求体可为空（不使用优惠券）")
    @PostMapping("/submit")
    public ApiResponse<OrderVO> submit(
            @RequestBody(required = false) SubmitOrderRequest request,
            @Parameter(hidden = true) HttpSession session) {
        Long userId = requireLogin(session);
        Long userCouponId = request == null ? null : request.getUserCouponId();
        return ApiResponse.success(orderService.submitOrder(userId, userCouponId));
    }

    @Operation(summary = "订单列表", description = "查询当前登录用户的所有订单")
    @GetMapping
    public ApiResponse<List<OrderVO>> listOrders(
            @Parameter(hidden = true) HttpSession session) {
        Long userId = requireLogin(session);
        return ApiResponse.success(orderService.listOrders(userId));
    }

    @Operation(summary = "订单详情", description = "根据订单ID查询订单详情（仅能查看自己的订单）")
    @GetMapping("/{orderId}")
    public ApiResponse<OrderVO> detail(
            @Parameter(description = "订单ID", example = "1001", required = true)
            @PathVariable Long orderId,
            @Parameter(hidden = true) HttpSession session) {
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
