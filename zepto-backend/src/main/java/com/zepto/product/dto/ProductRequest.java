package com.zepto.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private BigDecimal price;

    private BigDecimal discountPrice;

//    @NotNull(message = "Stock is required")
//    @Min(value = 0, message = "Stock cannot be negative")
//    private Integer stock;

    private String brand;

    private String unit;

    private String imageUrl;

    @NotNull(message = "Category is required")
    private Long categoryId;
}