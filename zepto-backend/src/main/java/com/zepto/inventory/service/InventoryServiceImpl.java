package com.zepto.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zepto.common.exception.ResourceAlreadyExistsException;
import com.zepto.common.exception.ResourceNotFoundException;
import com.zepto.inventory.dto.InventoryRequest;
import com.zepto.inventory.dto.InventoryResponse;
import com.zepto.inventory.entity.Inventory;
import com.zepto.inventory.entity.InventoryStatus;
import com.zepto.inventory.repository.InventoryRepository;
import com.zepto.product.entity.Product;
import com.zepto.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id : " + request.getProductId()));

        if (inventoryRepository.existsByProduct(product)) {
            throw new ResourceAlreadyExistsException(
                    "Inventory already exists for this product.");
        }

        Inventory inventory = Inventory.builder()
                .product(product)
                .totalQuantity(request.getQuantity())
                .reservedQuantity(request.getReservedQuantity())
                .warehouse(request.getWarehouse())
                .status(request.getStatus())
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }

    @Override
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponse getInventoryById(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with id : " + id));

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryByProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id : " + productId));

        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product id : " + productId));

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse updateInventory(Long id, InventoryRequest request) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with id : " + id));

        inventory.setTotalQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setWarehouse(request.getWarehouse());
        inventory.setStatus(request.getStatus());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) {

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with id : " + id));

        inventoryRepository.delete(inventory);
    }

    @Override
    public List<InventoryResponse> getLowStockProducts() {

        return inventoryRepository.findByTotalQuantityLessThanEqual(10)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryByStatus(String status) {

        InventoryStatus inventoryStatus =
                InventoryStatus.valueOf(status.toUpperCase());

        return inventoryRepository.findByStatus(inventoryStatus)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryByWarehouse(String warehouse) {

        return inventoryRepository.findByWarehouse(warehouse)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToResponse(Inventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProduct().getId())
                .productName(inventory.getProduct().getName())
                .quantity(inventory.getTotalQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(
                        inventory.getTotalQuantity() - inventory.getReservedQuantity())
                .warehouse(inventory.getWarehouse())
                .status(inventory.getStatus())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}