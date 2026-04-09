package com.example.campusstore.service;

import com.example.campusstore.dto.CreateOrderRequest;
import com.example.campusstore.dto.OrderItemRequest;
import com.example.campusstore.entity.*;
import com.example.campusstore.repository.OrderRepository;
import com.example.campusstore.repository.ProductRepository;
import com.example.campusstore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(Long customerId, CreateOrderRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> savedItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        boolean hasAtLeastOneItem = false;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Integer qty = itemRequest.getQty();

            if (qty == null || qty == 0) {
                continue;
            }

            if (qty < 0) {
                throw new RuntimeException("Quantity cannot be negative");
            }

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new RuntimeException("Product is inactive");
            }

            if (product.getStockQty() < qty) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStockQty(product.getStockQty() - qty);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQty(qty);
            orderItem.setUnitPrice(product.getPrice());

            savedItems.add(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(qty)));
            hasAtLeastOneItem = true;
        }

        if (!hasAtLeastOneItem) {
            throw new RuntimeException("Select at least one product");
        }

        order.setItems(savedItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Order> getMyOrders(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    public Order getMyOrderById(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("FORBIDDEN");
        }

        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new RuntimeException("Order is already in terminal state");
        }

        if (newStatus != OrderStatus.FULFILLED && newStatus != OrderStatus.CANCELLED) {
            throw new RuntimeException("Invalid status update");
        }

        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQty(product.getStockQty() + item.getQty());
            }
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}