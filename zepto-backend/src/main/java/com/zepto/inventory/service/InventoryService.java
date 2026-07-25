package com.zepto.inventory.service;

import java.util.List;

import com.zepto.inventory.dto.InventoryRequest;
import com.zepto.inventory.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    List<InventoryResponse> getAllInventory();

    InventoryResponse getInventoryById(Long id);

    InventoryResponse getInventoryByProduct(Long productId);

    InventoryResponse updateInventory(Long id, InventoryRequest request);

    void deleteInventory(Long id);

    List<InventoryResponse> getLowStockProducts();

    List<InventoryResponse> getInventoryByStatus(String status);

    List<InventoryResponse> getInventoryByWarehouse(String warehouse);

}