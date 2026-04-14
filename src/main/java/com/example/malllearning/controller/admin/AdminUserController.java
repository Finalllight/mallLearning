package com.example.malllearning.controller.admin;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.admin.AdminUserBalanceRequest;
import com.example.malllearning.dto.admin.AdminUserResponse;
import com.example.malllearning.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理员 - 用户管理", description = "用户列表、详情、余额修改（需管理员权限）")
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "用户列表")
    @GetMapping
    public ApiResponse<List<AdminUserResponse>> listAll() {
        return ApiResponse.success(adminService.listAllUsers());
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{userId}")
    public ApiResponse<AdminUserResponse> detail(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return ApiResponse.success(adminService.getUserDetail(userId));
    }

    @Operation(summary = "修改用户余额")
    @PutMapping("/{userId}/balance")
    public ApiResponse<AdminUserResponse> updateBalance(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Valid @RequestBody AdminUserBalanceRequest request) {
        return ApiResponse.success(adminService.updateUserBalance(userId, request.getBalance()));
    }
}
