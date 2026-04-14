package com.example.malllearning.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理员 - 创建/更新商品请求")
@Data
public class AdminProductRequest {

    @Schema(description = "商品名称", example = "MacBook Pro 14")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称最多100字")
    private String name;

    @Schema(description = "商品描述", example = "Apple M3 Pro 芯片")
    private String description;

    @Schema(description = "商品价格", example = "14999.00")
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @Schema(description = "库存数量", example = "200")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;
}
