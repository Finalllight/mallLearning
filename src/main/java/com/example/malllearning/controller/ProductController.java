package com.example.malllearning.controller;


import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.product.ProductResponse;
import com.example.malllearning.entity.Product;
import com.example.malllearning.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 商品列表
    @GetMapping
    public ApiResponse<List<ProductResponse>> list(){
        return ApiResponse.success(
                productService.getAllProducts()
                        .stream()
                        .map(ProductResponse::from)
                        .toList()
        );
    }

    // 商品详情
    @GetMapping("/{id}")
    public Map<String,Object> detail(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        try{
            Product product = productService.getProductById(id);
            response.put("status","success");
            response.put("product",product);
        }catch (Exception e){
            response.put("status","error");
            response.put("message",e.getMessage());
        }
        return response;
    }

    // 商品搜索
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword){
        return productService.searchProducts(keyword);
    }

}
