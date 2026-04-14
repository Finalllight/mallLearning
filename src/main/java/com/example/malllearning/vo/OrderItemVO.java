package com.example.malllearning.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "订单商品项")
public class OrderItemVO {

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "商品名称", example = "iPhone 15 Pro")
    private String productName;

    @Schema(description = "商品单价", example = "7999.00")
    private BigDecimal price;

    @Schema(description = "购买数量", example = "2")
    private Integer quantity;

    @Schema(description = "小计金额（单价 × 数量）", example = "15998.00")
    private BigDecimal subTotal;

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getSubTotal() { return subTotal; }

    public void setProductId(Long productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
}
