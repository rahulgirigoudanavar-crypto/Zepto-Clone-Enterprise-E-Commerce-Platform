package com.zepto.inventory.dto;

import com.zepto.inventory.entity.InventoryStatus;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Reserved Quantity is required")
    @Min(value = 0, message = "Reserved Quantity cannot be negative")
    private Integer reservedQuantity;

    @NotBlank(message = "Warehouse is required")
    private String warehouse;

    @NotNull(message = "Inventory status is required")
    private InventoryStatus status;
}