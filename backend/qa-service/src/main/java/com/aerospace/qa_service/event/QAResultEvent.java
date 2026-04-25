package com.aerospace.qa_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is the event published to "compliance.qa.result" topic
// inventory-service InventoryConsumer.consumeQAResult() deserializes this
// It MUST match QAStatusUpdatedEvent.java fields in inventory-service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QAResultEvent {
    private Long partId;
    private String batchNumber;
    private String status;          // "APPROVED" or "REJECTED" — matches StockStatus enum in inventory
    private String location;        // bin location — inventory needs this to find the right StockBalance row
    private String remarks;         // passed through so inventory transaction log has the QA note
    private String certificateNumber; // for full audit trail in inventory transaction
    private String inspectorName;
}