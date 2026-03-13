package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.cart.AddCartItemRequest;
import com.example.malllearning.dto.cart.UpdateCartItemRequest;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.service.CartService;
import com.example.malllearning.vo.CartVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ApiResponse<CartVO> addItem(@RequestBody AddCartItemRequest request, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401,"请先登录");

        CartVO cartVO = cartService.addItem(userId, request.getProductId(), request.getQuantity());
        return ApiResponse.success(cartVO);

    }

    @PutMapping("/items/{productId}")
    public ApiResponse<CartVO> updateItem(
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequest request,
            HttpSession session) {

        Long userId = requireLogin(session);
        CartVO cartVO = cartService.updateItemQuantity(userId, productId, request.getQuantity());
        return ApiResponse.success(cartVO);

    }

    @DeleteMapping("/items/{productId}")
    public ApiResponse<CartVO> removeItem(@PathVariable Long productId, HttpSession session) {
        Long userId = requireLogin(session);
        CartVO cartVO = cartService.removeItem(userId, productId);
        return ApiResponse.success(cartVO);

    }

    @GetMapping
    public ApiResponse<CartVO> getCart(HttpSession session) {
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
