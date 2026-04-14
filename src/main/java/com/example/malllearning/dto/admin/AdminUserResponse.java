package com.example.malllearning.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理员 - 用户信息响应")
@Data
public class AdminUserResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "余额")
    private BigDecimal balance;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
}
