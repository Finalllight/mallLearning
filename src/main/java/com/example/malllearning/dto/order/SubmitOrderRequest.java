package com.example.malllearning.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "提交订单请求")
public class SubmitOrderRequest {

    @Schema(description = "用户优惠券ID（user_coupon 表的ID，非 coupon 表ID），不传则不使用优惠券",
            example = "1",
            nullable = true,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Positive(message = "userCouponId必须大于0")
    private Long userCouponId;

    public Long getUserCouponId() { return userCouponId; }
    public void setUserCouponId(Long userCouponId) { this.userCouponId = userCouponId; }
}
