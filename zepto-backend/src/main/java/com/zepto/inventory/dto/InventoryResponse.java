package com.zepto.inventory.dto;

import java.time.LocalDateTime;

import com.zepto.inventory.entity.InventoryStatus;

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
public class InventoryResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;

    private Integer reservedQuantity;

    private Integer availableQuantity;

    private String warehouse;

    private InventoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}