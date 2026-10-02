package com.firstamerican.portal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "underwriting_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "order_status", nullable = false)
    private String status;

    @Column(name = "assigned_team")
    private String assignedTeam;

    @Column(name = "property_address")
    private String propertyAddress;
}