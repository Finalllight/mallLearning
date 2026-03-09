package com.example.malllearning.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddCartItemRequest {

    @NotNull(message = "productId不能为空")
    @Positive(message = "productId必须大于0")
    private Long productId;

    @NotNull(message = "quantity不能为空")
    @Min(value = 1, message = "quantity必须大于等于1")
    private Integer quantity;

    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

