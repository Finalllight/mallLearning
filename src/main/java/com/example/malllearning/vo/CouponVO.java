package com.example.malllearning.vo;

import java.math.BigDecimal;

public class CouponVO {

    private Long userCouponId;     // 用户领券记录ID（我的券场景）
    private Long couponId;
    private String name;
    private String type;
    private BigDecimal amount;
    private BigDecimal minOrder;
    private Integer quantity;      // 平台券列表场景
    private String status;         // 我的券场景

    private Boolean canUse;        // 针对某订单金额时是否可用
    private BigDecimal discount;   // 针对某订单金额可减金额

    public Long getUserCouponId() { return userCouponId; }
    public Long getCouponId() { return couponId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getMinOrder() { return minOrder; }
    public Integer getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public Boolean getCanUse() { return canUse; }
    public BigDecimal getDiscount() { return discount; }

    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setMinOrder(BigDecimal minOrder) { this.minOrder = minOrder; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setStatus(String status) { this.status = status; }
    public void setCanUse(Boolean canUse) { this.canUse = canUse; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
}
