package com.example.campusstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        Object userId = session.getAttribute("userId");
        Object role = session.getAttribute("role");

        // If not logged in, go to login page
        if (userId == null) {
            return "redirect:/login";
        }

        // If logged in but not admin, show forbidden page
        if (role == null || !role.toString().equals("ADMIN")) {
            return "error/403";
        }

        // If admin, open dashboard page
        return "admin/dashboard";
    }
}