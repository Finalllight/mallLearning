package com.example.malllearning.repository;

import com.example.malllearning.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.id = :cartId")
    List<CartItem> findByCart_IdWithProduct(@Param("cartId") Long cartId);

    List<CartItem> findByCart_Id(Long cartId);

    void deleteByCart_Id(Long cartId);
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
}
