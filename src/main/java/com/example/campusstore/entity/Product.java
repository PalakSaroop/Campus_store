package com.example.campusstore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQty;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // Default constructor
    public Product() {}

    // Constructor (useful for seeding)
    public Product(String name, String description, BigDecimal price, Integer stockQty, Boolean isActive, Category category) {
        this.setName(name);
        this.setPrice(price);
        this.setStockQty(stockQty);
        this.setCategory(category);
        this.description = description;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {   // important
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        this.price = price;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        if (stockQty == null || stockQty < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
        this.stockQty = stockQty;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Product must belong to a category");
        }
        this.category = category;
    }
}