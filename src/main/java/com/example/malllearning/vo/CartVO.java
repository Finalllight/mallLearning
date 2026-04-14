package com.example.malllearning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "购物车信息")
@Data
public class CartVO {

    @Schema(description = "购物车ID", example = "1")
    private Long cartId;

    @Schema(description = "购物车商品列表")
    private List<CartItemVO> items = new ArrayList<>();

    @Schema(description = "购物车总金额", example = "15998.00")
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
