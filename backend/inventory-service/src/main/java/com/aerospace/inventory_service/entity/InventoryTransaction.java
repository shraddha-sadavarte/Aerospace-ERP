package com.aerospace.inventory_service.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long partId;
    private String batchNumber;
    
    // ERP Transaction Types: GRN_RECEIPT, QA_APPROVE, PRODUCTION_ISSUE, ADJUSTMENT
    private String transactionType; 
    
    private int quantity; // Positive for IN, Negative for OUT
    
    private String referenceDoc; // e.g., GRN Number or Production Order ID
    
    private String remarks;
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
