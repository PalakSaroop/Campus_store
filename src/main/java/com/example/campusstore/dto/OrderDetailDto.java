package com.example.campusstore.dto;

import com.example.campusstore.entity.Order;
import com.example.campusstore.entity.OrderItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailDto {

    private Long orderId;
    private LocalDateTime createdAt;
    private String status;
    private BigDecimal total;
    private List<OrderItem> items;

    public OrderDetailDto(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt();
        this.status = order.getStatus().name();
        this.total = order.getTotal();
        this.items = order.getItems();
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}