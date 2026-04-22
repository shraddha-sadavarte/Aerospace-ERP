package com.aerospace.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String skuCode; // ERP standard: unique internal code

    private String partType; // e.g., "RAW_MATERIAL", "CONSUMABLE"
    private String uom;      // Unit of Measure: "EA", "KG", "LTR"
    private String criticality; // LOW, MEDIUM, HIGH
    private Integer reorderLevel;
    
    // Total quantity is now a derived value or managed via StockBalance logic
}
