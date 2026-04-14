package com.example.malllearning.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户登录响应")
@Data
public class LoginResponse {

    @Schema(description = "登录状态", example = "success")
    private String status;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户角色", example = "USER")
    private String role;   // ✅ 新增
}
