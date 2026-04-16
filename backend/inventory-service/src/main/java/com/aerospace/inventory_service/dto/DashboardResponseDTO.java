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

    private Map<String, Long> partsByType; // e.g Engine=10, Bolt=50
}