package com.example.malllearning.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理员 - 创建/更新优惠券请求")
@Data
public class AdminCouponRequest {

    @Schema(description = "优惠券名称", example = "满200减30")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Schema(description = "类型：FULL_REDUCTION(满减), DISCOUNT(折扣)", example = "FULL_REDUCTION")
    @NotBlank(message = "类型不能为空")
    private String type;

    @Schema(description = "优惠金额/折扣比例", example = "30.00")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @Schema(description = "最低订单金额(满减门槛)", example = "200.00")
    private BigDecimal minOrder;

    @Schema(description = "发行数量", example = "100")
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    private Integer quantity;
}
