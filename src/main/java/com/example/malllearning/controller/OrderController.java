package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.order.SubmitOrderRequest;
import com.example.malllearning.service.OrderService;
import com.example.malllearning.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "提交订单")
    @PostMapping("/submit")
    public ApiResponse<OrderVO> submit(
            @RequestBody(required = false) SubmitOrderRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        Long userId = getLoginUserId(httpRequest);
        Long userCouponId = request == null ? null : request.getUserCouponId();
        return ApiResponse.success(orderService.submitOrder(userId, userCouponId));
    }

    @Operation(summary = "订单列表")
    @GetMapping
    public ApiResponse<List<OrderVO>> listOrders(
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        Long userId = getLoginUserId(httpRequest);
        return ApiResponse.success(orderService.listOrders(userId));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{orderId}")
    public ApiResponse<OrderVO> detail(
            @Parameter(description = "订单ID", example = "1001", required = true)
            @PathVariable Long orderId,
            @Parameter(hidden = true) HttpServletRequest httpRequest) {
        Long userId = getLoginUserId(httpRequest);
        return ApiResponse.success(orderService.getOrderDetail(userId, orderId));
    }

    private Long getLoginUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("loginUserId");
    }
}
