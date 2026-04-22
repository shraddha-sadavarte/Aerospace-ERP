package com.aerospace.inventory_service.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardResponseDTO {
    private long totalParts;
    private long totalQuantity;
    private long lowStockItems;
    private long outOfStockItems;
    
    // ERP Specific Metrics
    private long pendingQAItems;    // NEW: Count of items in PENDING_QA status
    private long rejectedItems;     // NEW: Count of items in REJECTED status
    private double inventoryValue;  // NEW: Total value (if price is added later)

    private Map<String, Long> partsByType; 
}
