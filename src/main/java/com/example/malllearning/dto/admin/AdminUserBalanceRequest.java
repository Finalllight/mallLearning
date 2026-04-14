package com.example.malllearning.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理员 - 修改用户余额请求")
@Data
public class AdminUserBalanceRequest {

    @Schema(description = "新余额", example = "10000.00")
    @DecimalMin(value = "0.00", message = "余额不能为负数")
    private BigDecimal balance;
}
