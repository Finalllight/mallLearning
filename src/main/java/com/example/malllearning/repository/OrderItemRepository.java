package com.example.malllearning.repository;
import com.example.malllearning.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrder_IdWithProduct(@Param("orderId") Long orderId);
    List<OrderItem> findByOrder_Id(Long orderId);

    boolean existsByProduct_Id(Long ProductId);
}
