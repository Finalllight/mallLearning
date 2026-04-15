package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.cart.AddCartItemRequest;
import com.example.malllearning.dto.cart.UpdateCartItemRequest;
import com.example.malllearning.security.LoginUser;
import com.example.malllearning.service.CartService;
import com.example.malllearning.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车管理", description = "购物车的增删改查操作")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/items")
    public ApiResponse<CartVO> addItem(
            @Valid @RequestBody AddCartItemRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        CartVO cartVO = cartService.addItem(
                loginUser.getUserId(),
                request.getProductId(),
                request.getQuantity());
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/items/{productId}")
    public ApiResponse<CartVO> updateItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal LoginUser loginUser) {
        CartVO cartVO = cartService.updateItemQuantity(
                loginUser.getUserId(), productId, request.getQuantity());
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/items/{productId}")
    public ApiResponse<CartVO> removeItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal LoginUser loginUser) {
        CartVO cartVO = cartService.removeItem(loginUser.getUserId(), productId);
        return ApiResponse.success(cartVO);
    }

    @Operation(summary = "查看购物车")
    @GetMapping
    public ApiResponse<CartVO> getCart(
            @AuthenticationPrincipal LoginUser loginUser) {
        CartVO cartVO = cartService.getCart(loginUser.getUserId());
        return ApiResponse.success(cartVO);
    }

    // ✅ 不再需要 getLoginUserId() 私有方法了
}
