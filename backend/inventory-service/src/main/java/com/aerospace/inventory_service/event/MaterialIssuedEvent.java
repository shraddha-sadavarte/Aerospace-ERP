package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialIssuedEvent {
    private Long materialId;
    private String batchNumber;
    private Integer quantity;
    private String issuedTo; // Department or user
    private String workOrderId;
    private String location;
}
