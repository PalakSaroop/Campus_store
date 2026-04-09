package com.example.campusstore.controller;

import com.example.campusstore.entity.OrderStatus;
import com.example.campusstore.service.OrderService;
import com.example.campusstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    private final SessionUtil sessionUtil;

    public AdminOrderController(OrderService orderService, SessionUtil sessionUtil) {
        this.orderService = orderService;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping
    public String showAllOrders(HttpSession session, Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(role)) {
            return "error/403";
        }

        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session,
                               Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(role)) {
            return "error/403";
        }

        try {
            orderService.updateOrderStatus(id, OrderStatus.valueOf(status));
            return "redirect:/admin/orders";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("orders", orderService.getAllOrders());
            return "admin/orders";
        }
    }
}