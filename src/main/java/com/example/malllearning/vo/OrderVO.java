package com.example.malllearning.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "订单信息")
public class OrderVO {

    @Schema(description = "订单ID", example = "1001")
    private Long orderId;

    @Schema(description = "订单原始总金额", example = "15998.00")
    private BigDecimal totalAmount;

    @Schema(description = "优惠券抵扣金额", example = "20.00")
    private BigDecimal discount;

    @Schema(description = "实付金额（总金额 - 优惠）", example = "15978.00")
    private BigDecimal finalAmount;

    @Schema(description = "订单状态", example = "PAID")
    private String status;

    @Schema(description = "使用的优惠券ID", example = "10", nullable = true)
    private Long couponId;

    @Schema(description = "订单创建时间", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "支付后账户余额（仅提交订单时返回）", example = "5000.00", nullable = true)
    private BigDecimal balanceAfterPay;

    @Schema(description = "订单商品列表")
    private List<OrderItemVO> items = new ArrayList<>();

    public Long getOrderId() { return orderId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public String getStatus() { return status; }
    public Long getCouponId() { return couponId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BigDecimal getBalanceAfterPay() { return balanceAfterPay; }
    public List<OrderItemVO> getItems() { return items; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setBalanceAfterPay(BigDecimal balanceAfterPay) { this.balanceAfterPay = balanceAfterPay; }
    public void setItems(List<OrderItemVO> items) { this.items = items; }
}
