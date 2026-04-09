package com.example.campusstore.controller;

import com.example.campusstore.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        String result = authService.register(name, email, password);

        if (!result.equals("SUCCESS")) {
            model.addAttribute("error", result);
            return "auth/register";
        }

        model.addAttribute("success", "Registration successful. Please login.");
        return "auth/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

   @PostMapping("/login")
public String loginUser(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
    String result = authService.login(email, password, session);

    if (!result.equals("SUCCESS")) {
        model.addAttribute("error", result);
        return "auth/login";
    }

    String role = authService.getCurrentUserRole(session);

    if ("ADMIN".equals(role)) {
        return "redirect:/admin/dashboard";
    }

    return "redirect:/catalog";
}

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/login";
    }
}