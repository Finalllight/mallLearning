package com.example.malllearning.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderVO {
    private Long orderId;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String status;
    private Long couponId;
    private LocalDateTime createdAt;
    private BigDecimal balanceAfterPay; // 仅提交订单时有值

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
