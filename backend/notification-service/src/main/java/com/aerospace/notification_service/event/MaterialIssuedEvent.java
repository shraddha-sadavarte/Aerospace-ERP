package com.aerospace.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Matches MaterialIssuedEvent in inventory-service
// Published to: inventory.material.issued
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialIssuedEvent {
    private Long materialId;
    private String batchNumber;
    private Integer quantity;
    private String issuedTo;
    private String workOrderId;
    private String location;
}