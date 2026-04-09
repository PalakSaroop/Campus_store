package com.example.campusstore.repository;

import com.example.campusstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get all active products (simple case)
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // Filter by name (search)
    Page<Product> findByIsActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

    // Filter by category
    Page<Product> findByIsActiveTrueAndCategory_Id(Long categoryId, Pageable pageable);

    // Filter by name + category
    Page<Product> findByIsActiveTrueAndNameContainingIgnoreCaseAndCategory_Id(
            String name, Long categoryId, Pageable pageable);

    // Filter by in-stock products only
    Page<Product> findByIsActiveTrueAndStockQtyGreaterThan(Integer qty, Pageable pageable);

    // Full combination (name + category + stock)
    Page<Product> findByIsActiveTrueAndNameContainingIgnoreCaseAndCategory_IdAndStockQtyGreaterThan(
            String name, Long categoryId, Integer qty, Pageable pageable);
}