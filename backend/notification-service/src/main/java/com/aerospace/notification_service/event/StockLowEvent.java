package com.aerospace.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Matches StockLowEvent in inventory-service
// Published to: inventory.stock.low
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLowEvent {
    private Long partId;
    private String partName;
    private Integer currentQty;
    private Integer reorderLevel;
}