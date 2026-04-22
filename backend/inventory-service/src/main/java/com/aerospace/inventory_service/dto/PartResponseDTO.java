package com.aerospace.inventory_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartResponseDTO {
    private Long id;
    private String name;
    private String serialNumber;
    private String batchNumber;   // Added
    private Integer quantity;
    private String location;
    private String partType;
    private String criticality;   // Added
    private Integer reorderLevel; // Added
    private String status;        // Maps to qaStatus
    private String qaRemarks;     // Added
    private LocalDateTime expiryDate;
}
