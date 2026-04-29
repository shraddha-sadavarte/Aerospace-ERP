package com.aerospace.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Matches PartCreatedEvent in inventory-service
// Published to: inventory.created
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReceivedEvent {
    private Long id;
    private String name;
    private String serialNumber;
}