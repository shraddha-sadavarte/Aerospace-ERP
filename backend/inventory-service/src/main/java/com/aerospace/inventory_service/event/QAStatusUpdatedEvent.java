package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QAStatusUpdatedEvent {
    private Long partId;
    private String status; // APPROVED, REJECTED, PENDING_QA
    private String batchNumber;
    private String location;
}
