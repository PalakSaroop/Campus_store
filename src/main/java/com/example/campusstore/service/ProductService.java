package com.example.campusstore.service;

import com.example.campusstore.entity.Product;
import com.example.campusstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create product
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    // Deactivate product (soft delete)
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setIsActive(false);
        productRepository.save(product);
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // SEARCH + FILTER + PAGINATION (VERY IMPORTANT for T05)
    public Page<Product> searchProducts(String name,
                                        Long categoryId,
                                        Boolean inStock,
                                        Pageable pageable) {

        // Default name (empty search)
        if (name == null) name = "";

        // If category + name + inStock
        if (categoryId != null && inStock != null && inStock) {
            return productRepository
                    .findByIsActiveTrueAndNameContainingIgnoreCaseAndCategory_IdAndStockQtyGreaterThan(
                            name, categoryId, 0, pageable);
        }

        // If category + name
        if (categoryId != null) {
            return productRepository
                    .findByIsActiveTrueAndNameContainingIgnoreCaseAndCategory_Id(
                            name, categoryId, pageable);
        }

        // If only name
        if (name != null && !name.isEmpty()) {
            return productRepository
                    .findByIsActiveTrueAndNameContainingIgnoreCase(
                            name, pageable);
        }

        // If only in-stock
        if (inStock != null && inStock) {
            return productRepository
                    .findByIsActiveTrueAndStockQtyGreaterThan(0, pageable);
        }

        // Default → all active products
        return productRepository.findByIsActiveTrue(pageable);
    }
}