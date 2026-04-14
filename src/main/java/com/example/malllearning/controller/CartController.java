package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.cart.AddCartItemRequest;
import com.example.malllearning.dto.cart.UpdateCartItemRequest;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.service.CartService;
import com.example.malllearning.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车管理", description = "购物车的增删改查操作")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "添加商品到购物车", description = "向购物车中添加指定商品和数量，如果商品已存在则累加数量")
    @PostMapping("/items")
    public ApiResponse<CartVO> addItem(
            @RequestBody AddCartItemRequest request,
            @Parameter(hidden = true) HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401, "请先登录");
        CartVO cartVO = cartService.addItem(userId, request.getProductId(), request.getQuantity());
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "更新购物车商品数量", description = "根据商品ID更新购物车中该商品的数量")
    @PutMapping("/items/{productId}")
    public ApiResponse<CartVO> updateItem(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequest request,
            @Parameter(hidden = true) HttpSession session) {
        Long userId = requireLogin(session);
        CartVO cartVO = cartService.updateItemQuantity(userId, productId, request.getQuantity());
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "删除购物车商品", description = "根据商品ID从购物车中移除该商品")
    @DeleteMapping("/items/{productId}")
    public ApiResponse<CartVO> removeItem(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable Long productId,
            @Parameter(hidden = true) HttpSession session) {
        Long userId = requireLogin(session);
        CartVO cartVO = cartService.removeItem(userId, productId);
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "查看购物车", description = "获取当前登录用户的购物车信息，包含商品列表和总金额")
    @GetMapping
    public ApiResponse<CartVO> getCart(
            @Parameter(hidden = true) HttpSession session) {
        Long userId = requireLogin(session);
        CartVO cartVO = cartService.getCart(userId);
        return ApiResponse.success(cartVO);
    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return null;
        if (userId instanceof Number) return ((Number) userId).longValue();
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
