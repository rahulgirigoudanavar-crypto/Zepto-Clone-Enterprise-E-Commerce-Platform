package com.zepto.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zepto.category.entity.Category;
import com.zepto.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);

    boolean existsByNameIgnoreCase(String name);

}