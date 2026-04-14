package com.example.malllearning.controller.admin;

import com.example.malllearning.common.ApiResponse;
import com.example.malllearning.dto.admin.AdminProductRequest;
import com.example.malllearning.dto.product.ProductResponse;
import com.example.malllearning.entity.Product;
import com.example.malllearning.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员 - 商品管理", description = "商品的增删改操作（需管理员权限）")
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminService adminService;

    public AdminProductController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public ApiResponse<ProductResponse> create(@Valid @RequestBody AdminProductRequest request) {
        Product product = adminService.createProduct(request);
        return ApiResponse.success(ProductResponse.from(product));
    }

    @Operation(summary = "修改商品")
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(
            @Parameter(description = "商品ID") @PathVariable Long id,
            @Valid @RequestBody AdminProductRequest request) {
        Product product = adminService.updateProduct(id, request);
        return ApiResponse.success(ProductResponse.from(product));
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @Parameter(description = "商品ID") @PathVariable Long id) {
        adminService.deleteProduct(id);
        return ApiResponse.success();
    }
}
