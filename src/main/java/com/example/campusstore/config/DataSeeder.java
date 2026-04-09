package com.example.campusstore.config;

import com.example.campusstore.entity.Category;
import com.example.campusstore.entity.Product;
import com.example.campusstore.entity.Role;
import com.example.campusstore.entity.User;
import com.example.campusstore.repository.CategoryRepository;
import com.example.campusstore.repository.ProductRepository;
import com.example.campusstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        if (categoryRepository.count() == 0) {
            Category electronics = new Category();
            electronics.setName("Electronics");
            categoryRepository.save(electronics);

            Category stationery = new Category();
            stationery.setName("Stationery");
            categoryRepository.save(stationery);

            Product p1 = new Product();
            p1.setName("Mouse");
            p1.setDescription("Lenovo");
            p1.setPrice(new BigDecimal("15.00"));
            p1.setStockQty(10);
            p1.setIsActive(true);
            p1.setCategory(electronics);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Notebook");
            p2.setDescription("A4 notebook");
            p2.setPrice(new BigDecimal("5.00"));
            p2.setStockQty(20);
            p2.setIsActive(true);
            p2.setCategory(stationery);
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setName("Pen");
            p3.setDescription("Trimax");
            p3.setPrice(new BigDecimal("2.50"));
            p3.setStockQty(50);
            p3.setIsActive(true);
            p3.setCategory(stationery);
            productRepository.save(p3);
        }
    }
}