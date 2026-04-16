package com.aerospace.inventory_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class InventoryTransaction {
    @Id
    @GeneratedValue
    private Long id;

    private Long partId;
    private String type; // "IN" or "OUT"
    private int quantity;
    private LocalDateTime timestamp;
}
