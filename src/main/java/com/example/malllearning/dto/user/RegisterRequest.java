package com.example.malllearning.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "用户注册请求")
@Data
public class RegisterRequest {

    @Schema(description = "用户名", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456", minLength = 6, requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 6, message = "密码至少6位")
    private String password;

    @Schema(description = "邮箱", example = "zhangsan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "邮箱格式不正确")
    private String email;
}
