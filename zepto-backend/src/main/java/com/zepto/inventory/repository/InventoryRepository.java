package com.zepto.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zepto.inventory.entity.Inventory;
import com.zepto.inventory.entity.InventoryStatus;
import com.zepto.product.entity.Product;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);

    boolean existsByProduct(Product product);

    List<Inventory> findByStatus(InventoryStatus status);

    List<Inventory> findByTotalQuantityLessThanEqual(Integer quantity);

    List<Inventory> findByWarehouse(String warehouse);

}