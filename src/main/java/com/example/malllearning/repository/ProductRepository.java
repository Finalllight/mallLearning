package com.example.malllearning.repository;


import com.example.malllearning.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 商品关键字搜索
    List<Product> findByNameContaining(String keyword);
    Page<Product> findAll(Pageable pageable);
}
