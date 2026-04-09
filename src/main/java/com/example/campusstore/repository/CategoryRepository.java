package com.example.campusstore.repository;

import com.example.campusstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Check if category already exists (important for validation)
    boolean existsByNameIgnoreCase(String name);

    // Find category by name (useful for logic / seeding)
    Optional<Category> findByNameIgnoreCase(String name);
}