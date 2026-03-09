package com.example.malllearning.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartVO {

    private Long cartId;
    private List<CartItemVO> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public Long getCartId() {
        return cartId;
    }

    public List<CartItemVO> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setItems(List<CartItemVO> items) {
        this.items = items;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
