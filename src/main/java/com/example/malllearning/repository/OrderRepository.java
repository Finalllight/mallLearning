package com.example.malllearning.repository;

import com.example.malllearning.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByIdDesc(Long userId);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
