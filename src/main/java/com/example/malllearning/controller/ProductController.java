package com.example.malllearning.controller;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.product.ProductResponse;
import com.example.malllearning.entity.Product;
import com.example.malllearning.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品列表、详情、搜索等接口")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "商品列表", description = "获取所有商品列表")
    @GetMapping
    public ApiResponse<List<ProductResponse>> list() {
        return ApiResponse.success(
                productService.getAllProducts()
                        .stream()
                        .map(ProductResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "商品详情", description = "根据商品ID查询商品详情")
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> detail(
            @Parameter(description = "商品ID", example = "1", required = true)
            @PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ApiResponse.success(ProductResponse.from(product));
    }

    @Operation(summary = "商品搜索", description = "根据关键词搜索商品")
    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> search(
            @Parameter(description = "搜索关键词", example = "iPhone", required = true)
            @RequestParam String keyword) {
        return ApiResponse.success(
                productService.searchProducts(keyword)
                        .stream()
                        .map(ProductResponse::from)
                        .toList()
        );
    }
}
