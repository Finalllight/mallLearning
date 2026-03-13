package com.example.malllearning.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class CartVO {

    private Long cartId;
    private List<CartItemVO> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
