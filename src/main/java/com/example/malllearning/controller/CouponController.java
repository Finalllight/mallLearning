package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.service.CouponService;
import com.example.malllearning.vo.CouponVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {


    private final CouponService couponService;
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ApiResponse<List<CouponVO>> listAll() {
        List<CouponVO> coupons = couponService.listAllCoupons();
        return ApiResponse.success(coupons);
    }


    @GetMapping("/my")
    public ApiResponse<List<CouponVO>> myCoupons(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401,"请先登录");
        return ApiResponse.success(couponService.listMyCoupons(userId));
    }

    @GetMapping("/my/usable")
    public ApiResponse<List<CouponVO>> myUsableCoupons(@RequestParam BigDecimal orderAmount, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return ApiResponse.fail(401,"请先登录");
        try {
            return ApiResponse.success(couponService.listMyUsableCoupons(userId, orderAmount));
        } catch (Exception e) {
            return ApiResponse.fail(401,e.getMessage());
        }
    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        return null;
    }

}
