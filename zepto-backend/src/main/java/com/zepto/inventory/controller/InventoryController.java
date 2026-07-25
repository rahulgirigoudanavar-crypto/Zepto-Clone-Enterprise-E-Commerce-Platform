package com.zepto.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.zepto.inventory.dto.InventoryRequest;
import com.zepto.inventory.dto.InventoryResponse;
import com.zepto.inventory.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(request));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inventoryService.getInventoryById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProduct(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);

        return ResponseEntity.ok("Inventory deleted successfully.");
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockProducts() {

        return ResponseEntity.ok(
                inventoryService.getLowStockProducts());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InventoryResponse>> getInventoryByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByStatus(status));
    }

    @GetMapping("/warehouse")
    public ResponseEntity<List<InventoryResponse>> getInventoryByWarehouse(
            @RequestParam String warehouse) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByWarehouse(warehouse));
    }
}