package com.firstamerican.portal.controller;

import com.firstamerican.portal.model.Order;
import com.firstamerican.portal.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/track/{orderId}")
    public ResponseEntity<?> trackOrder(@PathVariable String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(order -> ResponseEntity.ok(Map.of(
                        "found", true,
                        "orderId", order.getOrderId(),
                        "status", order.getStatus(),
                        "message", String.format("Order ID #%s is currently %s by the %s.",
                                order.getOrderId(), order.getStatus(), order.getAssignedTeam())
                )))
                .orElse(ResponseEntity.ok(Map.of(
                        "found", false,
                        "message", String.format("Order ID #%s was not found in the database.", orderId)
                )));
    }
}