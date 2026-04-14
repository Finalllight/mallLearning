package com.example.malllearning.service;
import com.example.malllearning.common.ResultCode;
import com.example.malllearning.entity.Product;
import com.example.malllearning.exception.BusinessException;
import com.example.malllearning.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 获取商品列表
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // 获取商品详情
    public Product getProductById(Long id)  {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND, "商品不存在"));
    }

    // 搜索商品
    public List<Product> searchProducts(String keyword){
        return productRepository.findByNameContaining(keyword);
    }
}
