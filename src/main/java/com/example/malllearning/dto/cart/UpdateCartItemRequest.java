package com.example.malllearning.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Schema(description = "更新购物车商品请求")
public class UpdateCartItemRequest {

    @Schema(description = "更新后的商品数量", example = "2", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "quantity不能为空")
    @Min(value = 1, message = "quantity必须大于等于1")
    private Integer quantity;


    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
