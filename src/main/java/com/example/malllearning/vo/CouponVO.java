package com.example.malllearning.vo;

import com.example.malllearning.dto.coupon.CouponResponse;
import com.example.malllearning.entity.UserCoupon;
import lombok.Data;

import java.math.BigDecimal;
@Data
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

}
