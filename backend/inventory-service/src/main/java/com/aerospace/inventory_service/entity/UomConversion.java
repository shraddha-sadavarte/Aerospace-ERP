package com.aerospace.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UomConversion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String fromUom; // e.g., "KG"
    private String toUom; // e.g., "EA"
    private Double factor; // e.g., 1 KG = 10 EA means factor = 10.0
}
