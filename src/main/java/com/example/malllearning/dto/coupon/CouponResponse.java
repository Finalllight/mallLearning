package com.example.malllearning.dto.coupon;

import com.example.malllearning.entity.Coupon;
import com.example.malllearning.entity.Product;
import com.example.malllearning.entity.UserCoupon;
import com.example.malllearning.enums.CouponType;
import com.example.malllearning.enums.UserCouponStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponResponse {


    private Long id;
    private Long userID;
    private Coupon coupon;
    private UserCouponStatus status = UserCouponStatus.UNUSED;

    public static CouponResponse from(UserCoupon usercoupon){
        CouponResponse dto = new CouponResponse();
        dto.id = usercoupon.getId();
        dto.userID = usercoupon.getUserId();
        dto.coupon = usercoupon.getCoupon();
        return dto;
    }
}