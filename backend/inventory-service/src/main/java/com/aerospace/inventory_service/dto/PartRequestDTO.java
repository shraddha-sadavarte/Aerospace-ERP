package com.aerospace.inventory_service.dto;

import lombok.Data;

@Data
public class PartRequestDTO {
    private String name;
    private String serialNumber;
    private String batchNumber;
    private Integer quantity;
    private String location;
    private String partType;
    private String criticality;
    private Integer reorderLevel;
    private String status;    // Maps to qaStatus in Entity
    private String qaRemarks; // Added to capture remarks from React form
}
