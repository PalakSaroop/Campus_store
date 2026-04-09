package com.example.campusstore.controller;

import com.example.campusstore.entity.Category;
import com.example.campusstore.entity.Product;
import com.example.campusstore.service.CategoryService;
import com.example.campusstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Show product admin page
    @GetMapping
    public String showProducts(Model model) {
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());

        return "admin/products";
    }

    // Create product
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    // Update product
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product) {
        productService.updateProduct(product);
        return "redirect:/admin/products";
    }

    // Deactivate product (soft delete)
    @PostMapping("/deactivate/{id}")
    public String deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return "redirect:/admin/products";
    }
}