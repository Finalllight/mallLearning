package com.example.malllearning.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "优惠券信息")
@Data
public class CouponVO {

    @Schema(description = "用户领券记录ID（我的券场景）", example = "1")
    private Long userCouponId;

    @Schema(description = "优惠券ID", example = "10")
    private Long couponId;

    @Schema(description = "优惠券名称", example = "满100减20优惠券")
    private String name;

    @Schema(description = "优惠券类型（FULL_REDUCTION: 满减券, DISCOUNT: 折扣券）", example = "FULL_REDUCTION")
    private String type;


    @Schema(description = "优惠金额 / 折扣比例", example = "20.00")
    private BigDecimal amount;

    @Schema(description = "最低订单金额（满减门槛）", example = "100.00")
    private BigDecimal minOrder;

    @Schema(description = "剩余可领数量（平台券列表场景）", example = "50")
    private Integer quantity;

    @Schema(description = "优惠券使用状态（我的券场景）", example = "UNUSED")
    private String status;

    @Schema(description = "针对当前订单金额是否可用", example = "true")
    private Boolean canUse;

    @Schema(description = "针对当前订单金额可减金额", example = "20.00")
    private BigDecimal discount;
}
