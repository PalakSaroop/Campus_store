package com.example.campusstore.controller;

import com.example.campusstore.service.ProductService;
import com.example.campusstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.campusstore.entity.Product;

@Controller
public class CatalogController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping("/catalog")
    public String catalog(
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        // Not logged in
        if (userId == null) {
            return "redirect:/login";
        }

        // Wrong role
        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        // Sorting
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        // Get filtered products
        Page<Product> productPage = productService.searchProducts(
                name, categoryId, inStock, pageable
        );

        // Handle empty page
        if (page >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            model.addAttribute("message", "No results on this page");
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "catalog/list";
    }

    // Product details page
    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "catalog/details";
    }
}