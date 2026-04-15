package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.security.LoginUser;
import com.example.malllearning.service.CouponService;
import com.example.malllearning.vo.CouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "优惠券管理", description = "优惠券列表、我的优惠券、可用优惠券查询等接口")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "优惠券列表（公开）")
    @GetMapping
    public ApiResponse<List<CouponVO>> listAll() {
        List<CouponVO> coupons = couponService.listAllCoupons();
        return ApiResponse.success(coupons);
    }

    @Operation(summary = "领取优惠券")
    @PostMapping("/{couponId}/claim")
    public ApiResponse<Void> claimCoupon(
            @Parameter(description = "优惠券ID", example = "1", required = true)
            @PathVariable Long couponId,
            @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = loginUser.getUserId();
        couponService.claimCoupon(userId, couponId);
        return ApiResponse.success();
    }

    @Operation(summary = "我的优惠券")
    @GetMapping("/my")
    public ApiResponse<List<CouponVO>> myCoupons(
            @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = loginUser.getUserId();
        return ApiResponse.success(couponService.listMyCoupons(userId));
    }

    @Operation(summary = "可用优惠券列表")
    @GetMapping("/my/usable")
    public ApiResponse<List<CouponVO>> myUsableCoupons(
            @Parameter(description = "订单金额", example = "500.00", required = true)
            @RequestParam BigDecimal orderAmount,
            @AuthenticationPrincipal LoginUser loginUser) {
        Long userId = loginUser.getUserId();
        return ApiResponse.success(couponService.listMyUsableCoupons(userId, orderAmount));
    }

}
