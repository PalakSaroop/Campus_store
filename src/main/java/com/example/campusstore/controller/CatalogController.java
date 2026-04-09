package com.example.campusstore.controller;

import com.example.campusstore.repository.ProductRepository;
import com.example.campusstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {

    private final ProductRepository productRepository;
    private final SessionUtil sessionUtil;

    public CatalogController(ProductRepository productRepository, SessionUtil sessionUtil) {
        this.productRepository = productRepository;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping("/catalog")
    public String catalog(HttpSession session, Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        model.addAttribute("products", productRepository.findByIsActiveTrue());
        return "catalog";
    }
}