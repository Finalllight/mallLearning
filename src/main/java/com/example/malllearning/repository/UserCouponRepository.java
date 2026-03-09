package com.example.malllearning.repository;

import com.example.malllearning.entity.UserCoupon;
import com.example.malllearning.enums.UserCouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findByUserIdOrderByIdDesc(Long userId);

    List<UserCoupon> findByUserIdAndStatusOrderByIdDesc(Long userId, UserCouponStatus status);

    Optional<UserCoupon> findByIdAndUserId(Long id, Long userId);
}