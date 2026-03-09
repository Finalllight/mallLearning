package com.example.malllearning.repository;


import com.example.malllearning.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 为兼容已有表结构，取该用户第一条购物车记录
    Optional<Cart> findFirstByUserIdOrderByIdAsc(Long userId);
}
