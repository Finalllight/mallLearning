package com.example.malllearning.dto.product;

import com.example.malllearning.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    // constructor, getter, setter
    public static ProductResponse from(Product product){
        ProductResponse dto = new ProductResponse();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        dto.stock = product.getStock();
        return dto;
    }
}
