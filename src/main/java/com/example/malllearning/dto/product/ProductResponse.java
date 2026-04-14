package com.example.malllearning.dto.product;

import com.example.malllearning.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "商品信息响应")
@Data
public class ProductResponse {

    @Schema(description = "商品ID", example = "1")
    private Long id;

    @Schema(description = "商品名称", example = "iPhone 15 Pro")
    private String name;

    @Schema(description = "商品描述", example = "Apple iPhone 15 Pro 256GB 原色钛金属")
    private String description;

    @Schema(description = "商品价格", example = "7999.00")
    private BigDecimal price;

    @Schema(description = "库存数量", example = "100")
    private Integer stock;

    public static ProductResponse from(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        dto.stock = product.getStock();
        return dto;
    }
}
