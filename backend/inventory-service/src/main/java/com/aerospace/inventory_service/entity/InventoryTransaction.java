package com.aerospace.inventory_service.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*; // Added for boilerplate reduction

@Entity
@Data // Added to provide Getters, Setters, toString, etc.
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specified strategy for consistency
    private Long id;

    private Long partId;
    private String type; // "IN" or "OUT"
    private int quantity;
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
