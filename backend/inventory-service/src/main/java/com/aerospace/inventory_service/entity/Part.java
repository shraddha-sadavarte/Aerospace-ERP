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

    private String name;
    private String serialNumber;
    private String batchNumber;
    private Integer quantity;
    private String location;
    private String partType;
    private String criticality;
    private Integer reorderLevel;
}
