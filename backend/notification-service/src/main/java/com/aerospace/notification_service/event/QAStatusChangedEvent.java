package com.aerospace.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Matches QAStatusChangedEvent in inventory-service
// Published to: inventory.qa.status.updated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QAStatusChangedEvent {
    private Long partId;
    private String batchNumber;
    private String status;        // APPROVED or REJECTED
    private String remarks;
    private String inspectorName;
}