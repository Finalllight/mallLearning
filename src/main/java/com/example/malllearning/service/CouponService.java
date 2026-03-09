package com.example.malllearning.service;


import com.example.malllearning.entity.Coupon;
import com.example.malllearning.entity.UserCoupon;
import com.example.malllearning.enums.CouponType;
import com.example.malllearning.enums.UserCouponStatus;
import com.example.malllearning.repository.CouponRepository;
import com.example.malllearning.repository.UserCouponRepository;
import com.example.malllearning.vo.CouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final UserCouponRepository userCouponRepository;

    public CouponService(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional(readOnly = true)
    public List<CouponVO> listAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        List<CouponVO> result = new ArrayList<>();
        for (Coupon c : coupons) {
            CouponVO vo = new CouponVO();
            vo.setCouponId(c.getId());
            vo.setName(c.getName());
            vo.setType(c.getType().name());
            vo.setAmount(c.getAmount());
            vo.setMinOrder(c.getMinOrder());
            vo.setQuantity(c.getQuantity());
            result.add(vo);
        }
        return result;
    }

    @Transactional
    public void claimCoupon(Long userId, Long couponId) throws Exception {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new Exception("优惠券不存在"));

        if (coupon.getQuantity() == null || coupon.getQuantity() <= 0) {
            throw new Exception("优惠券已领完");
        }

        coupon.setQuantity(coupon.getQuantity() - 1);
        couponRepository.save(coupon);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCoupon(coupon);
        userCoupon.setStatus(UserCouponStatus.UNUSED);
        userCouponRepository.save(userCoupon);
    }

    @Transactional(readOnly = true)
    public List<CouponVO> listMyCoupons(Long userId) {
        List<UserCoupon> list = userCouponRepository.findByUserIdOrderByIdDesc(userId);
        List<CouponVO> result = new ArrayList<>();

        for (UserCoupon uc : list) {
            Coupon c = uc.getCoupon();
            CouponVO vo = new CouponVO();
            vo.setUserCouponId(uc.getId());
            vo.setCouponId(c.getId());
            vo.setName(c.getName());
            vo.setType(c.getType().name());
            vo.setAmount(c.getAmount());
            vo.setMinOrder(c.getMinOrder());
            vo.setStatus(uc.getStatus().name());
            result.add(vo);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<CouponVO> listMyUsableCoupons(Long userId, BigDecimal orderAmount) throws Exception {
        if (orderAmount == null || orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("订单金额必须大于0");
        }

        List<UserCoupon> list = userCouponRepository
                .findByUserIdAndStatusOrderByIdDesc(userId, UserCouponStatus.UNUSED);

        List<CouponVO> result = new ArrayList<>();
        for (UserCoupon uc : list) {
            BigDecimal discount = calculateDiscount(uc.getCoupon(), orderAmount);
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                Coupon c = uc.getCoupon();
                CouponVO vo = new CouponVO();
                vo.setUserCouponId(uc.getId());
                vo.setCouponId(c.getId());
                vo.setName(c.getName());
                vo.setType(c.getType().name());
                vo.setAmount(c.getAmount());
                vo.setMinOrder(c.getMinOrder());
                vo.setStatus(uc.getStatus().name());
                vo.setCanUse(true);
                vo.setDiscount(discount);
                result.add(vo);
            }
        }

        result.sort(Comparator.comparing(CouponVO::getDiscount).reversed());
        return result;
    }

    // 给订单模块调用：校验并计算优惠金额
    @Transactional(readOnly = true)
    public BigDecimal validateAndCalculateDiscount(Long userId, Long userCouponId, BigDecimal orderAmount) throws Exception {
        UserCoupon uc = userCouponRepository.findByIdAndUserId(userCouponId, userId)
                .orElseThrow(() -> new Exception("用户优惠券不存在"));

        if (uc.getStatus() != UserCouponStatus.UNUSED) {
            throw new Exception("优惠券不可用");
        }

        BigDecimal discount = calculateDiscount(uc.getCoupon(), orderAmount);
        if (discount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("不满足优惠券使用条件");
        }
        return discount;
    }

    // 给订单模块调用：下单成功后标记已使用
    @Transactional
    public void markCouponUsed(Long userId, Long userCouponId) throws Exception {
        UserCoupon uc = userCouponRepository.findByIdAndUserId(userCouponId, userId)
                .orElseThrow(() -> new Exception("用户优惠券不存在"));

        if (uc.getStatus() != UserCouponStatus.UNUSED) {
            throw new Exception("优惠券状态异常");
        }

        uc.setStatus(UserCouponStatus.USED);
        userCouponRepository.save(uc);
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
        // 统一满额门槛判断（minOrder 为空视为 0）
        BigDecimal minOrder = coupon.getMinOrder() == null ? BigDecimal.ZERO : coupon.getMinOrder();
        if (orderAmount.compareTo(minOrder) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;

        if (coupon.getType() == CouponType.FULL_REDUCTION) {
            discount = coupon.getAmount();
        } else if (coupon.getType() == CouponType.DISCOUNT) {
            // 约定：amount 用 0.90 表示 9折
            BigDecimal rate = coupon.getAmount();
            if (rate.compareTo(BigDecimal.ZERO) <= 0 || rate.compareTo(BigDecimal.ONE) >= 0) {
                return BigDecimal.ZERO;
            }
            discount = orderAmount.multiply(BigDecimal.ONE.subtract(rate));
        }

        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }
        return discount.setScale(2, RoundingMode.HALF_UP);
    }
}
