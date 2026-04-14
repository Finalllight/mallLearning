package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.service.CouponService;
import com.example.malllearning.vo.CouponVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
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

    @Operation(summary = "优惠券列表", description = "查询平台所有可领取的优惠券列表（无需登录）")
    @GetMapping
    public ApiResponse<List<CouponVO>> listAll() {
        List<CouponVO> coupons = couponService.listAllCoupons();
        return ApiResponse.success(coupons);
    }

    @Operation(summary = "我的优惠券", description = "查询当前登录用户已领取的所有优惠券")
    @GetMapping("/my")
    public ApiResponse<List<CouponVO>> myCoupons(
            @Parameter(hidden = true) HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401, "请先登录");
        return ApiResponse.success(couponService.listMyCoupons(userId));
    }

    @Operation(summary = "可用优惠券列表", description = "根据订单金额查询当前用户可使用的优惠券，返回每张券的可减金额")
    @GetMapping("/my/usable")
    public ApiResponse<List<CouponVO>> myUsableCoupons(
            @Parameter(description = "订单金额", example = "500.00", required = true)
            @RequestParam BigDecimal orderAmount,
            @Parameter(hidden = true) HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401, "请先登录");
        try {
            return ApiResponse.success(couponService.listMyUsableCoupons(userId, orderAmount));
        } catch (Exception e) {
            return ApiResponse.fail(401, e.getMessage());
        }
    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        return null;
    }
}
