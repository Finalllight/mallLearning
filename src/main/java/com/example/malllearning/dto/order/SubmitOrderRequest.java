package com.example.malllearning.dto.order;

import jakarta.validation.constraints.Positive;

public class SubmitOrderRequest {
    // 注意：这里是 user_coupon 表的ID（用户领到的券），不是 coupon 表ID
    @Positive(message = "userCouponId必须大于0")
    private Long userCouponId; // 可为空

    public Long getUserCouponId() { return userCouponId; }
    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }
}
