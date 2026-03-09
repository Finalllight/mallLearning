package com.example.malllearning.controller;

import com.example.malllearning.dto.cart.AddCartItemRequest;
import com.example.malllearning.dto.cart.UpdateCartItemRequest;
import com.example.malllearning.service.CartService;
import com.example.malllearning.vo.CartVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public Map<String, Object> addItem(@RequestBody AddCartItemRequest request, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");
        try {
            CartVO cartVO = cartService.addItem(userId, request.getProductId(), request.getQuantity());
            return success(cartVO);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @PutMapping("/items/{productId}")
    public Map<String, Object> updateItem(
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequest request,
            HttpSession session) {

        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        try {
            CartVO cartVO = cartService.updateItemQuantity(userId, productId, request.getQuantity());
            return success(cartVO);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @DeleteMapping("/items/{productId}")
    public Map<String, Object> removeItem(@PathVariable Long productId, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        try {
            CartVO cartVO = cartService.removeItem(userId, productId);
            return success(cartVO);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping
    public Map<String, Object> getCart(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        CartVO cartVO = cartService.getCart(userId);
        return success(cartVO);
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
