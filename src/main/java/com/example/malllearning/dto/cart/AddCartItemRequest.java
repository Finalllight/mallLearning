package com.example.malllearning.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "添加购物车商品请求")
public class AddCartItemRequest {

    @Schema(description = "商品ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "productId不能为空")
    @Positive(message = "productId必须大于0")
    private Long productId;

    @Schema(description = "商品数量", example = "1", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "quantity不能为空")
    @Min(value = 1, message = "quantity必须大于等于1")
    private Integer quantity;

    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
