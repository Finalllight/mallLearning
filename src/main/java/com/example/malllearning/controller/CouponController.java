package com.example.malllearning.controller;

import com.example.malllearning.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {


    private final CouponService couponService;
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }
    @GetMapping
    public Map<String, Object> listAll() {
        return success(couponService.listAllCoupons());
    }

    @PostMapping("/{couponId}/claim")
    public Map<String, Object> claim(@PathVariable Long couponId, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        try {
            couponService.claimCoupon(userId, couponId);
            return success("领取成功");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Map<String, Object> myCoupons(HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        return success(couponService.listMyCoupons(userId));
    }

    @GetMapping("/my/usable")
    public Map<String, Object> myUsableCoupons(@RequestParam BigDecimal orderAmount, HttpSession session) {
        Long userId = getLoginUserId(session);
        if (userId == null) return error("请先登录");

        try {
            return success(couponService.listMyUsableCoupons(userId, orderAmount));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private Long getLoginUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        return null;
    }

    private Map<String, Object> success(Object data) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "success");
        resp.put("data", data);
        return resp;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "error");
        resp.put("message", message);
        return resp;
    }
}
