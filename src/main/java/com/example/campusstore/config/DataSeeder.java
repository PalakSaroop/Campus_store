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

        // 🔹 Seed ADMIN (Khushi part)
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        // 🔹 Seed Categories + Products (Palak part)
        if (categoryRepository.count() == 0) {

            Category electronics = new Category();
            electronics.setName("Electronics");
            categoryRepository.save(electronics);

            Category stationery = new Category();
            stationery.setName("Stationery");
            categoryRepository.save(stationery);

            // 🔥 PRODUCTS (7 total)

            Product p1 = new Product();
            p1.setName("Mouse");
            p1.setDescription("Lenovo Mouse");
            p1.setPrice(new BigDecimal("15.00"));
            p1.setStockQty(10);
            p1.setIsActive(true);
            p1.setCategory(electronics);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Notebook");
            p2.setDescription("A4 Notebook");
            p2.setPrice(new BigDecimal("5.00"));
            p2.setStockQty(20);
            p2.setIsActive(true);
            p2.setCategory(stationery);
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setName("Pen");
            p3.setDescription("Trimax Pen");
            p3.setPrice(new BigDecimal("2.50"));
            p3.setStockQty(50);
            p3.setIsActive(true);
            p3.setCategory(stationery);
            productRepository.save(p3);

            Product p4 = new Product();
            p4.setName("Keyboard");
            p4.setDescription("Mechanical Keyboard");
            p4.setPrice(new BigDecimal("30.00"));
            p4.setStockQty(15);
            p4.setIsActive(true);
            p4.setCategory(electronics);
            productRepository.save(p4);

            Product p5 = new Product();
            p5.setName("Monitor");
            p5.setDescription("24 inch Display");
            p5.setPrice(new BigDecimal("120.00"));
            p5.setStockQty(8);
            p5.setIsActive(true);
            p5.setCategory(electronics);
            productRepository.save(p5);

            Product p6 = new Product();
            p6.setName("Highlighter");
            p6.setDescription("Set of 5 Colors");
            p6.setPrice(new BigDecimal("6.00"));
            p6.setStockQty(25);
            p6.setIsActive(true);
            p6.setCategory(stationery);
            productRepository.save(p6);

            Product p7 = new Product();
            p7.setName("Stapler");
            p7.setDescription("Mini Stapler");
            p7.setPrice(new BigDecimal("4.00"));
            p7.setStockQty(18);
            p7.setIsActive(true);
            p7.setCategory(stationery);
            productRepository.save(p7);
        }
    }
}