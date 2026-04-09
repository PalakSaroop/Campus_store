package com.example.campusstore.controller;

import com.example.campusstore.dto.CreateOrderRequest;
import com.example.campusstore.dto.OrderDetailDto;
import com.example.campusstore.dto.OrderItemRequest;
import com.example.campusstore.entity.Order;
import com.example.campusstore.entity.Product;
import com.example.campusstore.repository.ProductRepository;
import com.example.campusstore.service.OrderService;
import com.example.campusstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final SessionUtil sessionUtil;

    public OrderController(OrderService orderService,
                           ProductRepository productRepository,
                           SessionUtil sessionUtil) {
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.sessionUtil = sessionUtil;
    }

    @GetMapping("/create")
    public String showCreateOrderPage(HttpSession session, Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        // ✅ FIXED: using pageable method
        List<Product> products = productRepository
                .findByIsActiveTrue(PageRequest.of(0, 100))
                .getContent();

        CreateOrderRequest request = new CreateOrderRequest();
        List<OrderItemRequest> items = new ArrayList<>();

        for (Product product : products) {
            OrderItemRequest item = new OrderItemRequest();
            item.setProductId(product.getId());
            item.setQty(0);
            items.add(item);
        }

        request.setItems(items);

        model.addAttribute("products", products);
        model.addAttribute("createOrderRequest", request);
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute CreateOrderRequest createOrderRequest,
                             HttpSession session,
                             Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        try {
            Order order = orderService.createOrder(userId, createOrderRequest);
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());

            // ✅ FIXED: using pageable method
            model.addAttribute("products",
                    productRepository.findByIsActiveTrue(PageRequest.of(0, 100)).getContent());

            model.addAttribute("createOrderRequest", createOrderRequest);
            return "order/create";
        }
    }

    @GetMapping("/history")
    public String showOrderHistory(HttpSession session, Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        model.addAttribute("orders", orderService.getMyOrders(userId));
        return "order/history";
    }

    @GetMapping("/{id}")
    public String showOrderDetails(@PathVariable Long id,
                                  HttpSession session,
                                  Model model) {
        Long userId = sessionUtil.getCurrentUserId(session);
        String role = sessionUtil.getCurrentUserRole(session);

        if (userId == null) {
            return "redirect:/login";
        }

        if (!"CUSTOMER".equals(role)) {
            return "error/403";
        }

        try {
            Order order = orderService.getMyOrderById(id, userId);
            model.addAttribute("order", new OrderDetailDto(order));
            return "order/details";
        } catch (RuntimeException ex) {
            if ("FORBIDDEN".equals(ex.getMessage())) {
                return "error/403";
            }
            model.addAttribute("error", ex.getMessage());
            return "order/history";
        }
    }
}