package com.example.malllearning.dto.coupon;

import com.example.malllearning.entity.Coupon;
import com.example.malllearning.entity.UserCoupon;
import com.example.malllearning.enums.UserCouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户优惠券响应")
@Data
public class CouponResponse {

    @Schema(description = "用户优惠券记录ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", example = "100")
    private Long userID;

    @Schema(description = "优惠券详情")
    private Coupon coupon;

    @Schema(description = "优惠券使用状态", example = "UNUSED", defaultValue = "UNUSED")
    private UserCouponStatus status = UserCouponStatus.UNUSED;

    public static CouponResponse from(UserCoupon usercoupon) {
        CouponResponse dto = new CouponResponse();
        dto.id = usercoupon.getId();
        dto.userID = usercoupon.getUserId();
        dto.coupon = usercoupon.getCoupon();
        return dto;
    }
}
