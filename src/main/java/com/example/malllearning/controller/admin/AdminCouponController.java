package com.example.malllearning.controller.admin;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.admin.AdminCouponRequest;
import com.example.malllearning.vo.CouponVO;
import com.example.malllearning.entity.Coupon;
import com.example.malllearning.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员 - 优惠券管理", description = "优惠券的增删改操作（需管理员权限）")
@RestController
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {

    private final AdminService adminService;

    public AdminCouponController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "创建优惠券")
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody AdminCouponRequest request) {
        Coupon coupon = adminService.createCoupon(request);
        return ApiResponse.success(coupon.getId());
    }

    @Operation(summary = "修改优惠券")
    @PutMapping("/{id}")
    public ApiResponse<Long> update(
            @Parameter(description = "优惠券ID") @PathVariable Long id,
            @Valid @RequestBody AdminCouponRequest request) {
        Coupon coupon = adminService.updateCoupon(id, request);
        return ApiResponse.success(coupon.getId());
    }

    @Operation(summary = "删除优惠券")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @Parameter(description = "优惠券ID") @PathVariable Long id) {
        adminService.deleteCoupon(id);
        return ApiResponse.success();
    }
}
