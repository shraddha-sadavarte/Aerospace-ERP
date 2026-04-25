package com.aerospace.qa_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// QA service listens for this — when inventory receives a GRN,
// QA gets notified that a batch is waiting for inspection
// This matches PartCreatedEvent from inventory-service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReceivedEvent {
    private Long id;            // partId in inventory
    private String name;        // part name — for display in QA queue
    private String serialNumber; // SKU code
}