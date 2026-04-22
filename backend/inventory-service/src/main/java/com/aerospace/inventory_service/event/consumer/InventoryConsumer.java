package com.aerospace.inventory_service.event.consumer;

import com.aerospace.inventory_service.event.KafkaTopics;
import com.aerospace.inventory_service.event.PartCreatedEvent;
import com.aerospace.inventory_service.event.QAStatusUpdatedEvent; // FIX 2: updated import
import com.aerospace.inventory_service.service.PartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryConsumer {

    private final PartService partService;

    // Listens for new part creation confirmations from other services
    @KafkaListener(topics = KafkaTopics.INVENTORY_CREATED, groupId = "inventory-group")
    public void consumeCreated(PartCreatedEvent event) {
        log.info("Inventory record created in DB: {}", event.getName());
    }

    // Listens for QA decisions made by the Compliance/QA Service
    // Automatically moves stock from PENDING_QA → APPROVED or REJECTED
    @KafkaListener(topics = KafkaTopics.COMPLIANCE_QA_RESULT, groupId = "inventory-group")
    public void consumeQAResult(QAStatusUpdatedEvent event) {
        log.info("Received external QA result for Part ID {}, Batch {}, Status {}",
                event.getPartId(), event.getBatchNumber(), event.getStatus());

        try {
            partService.processQADecision(
                    event.getPartId(),
                    event.getBatchNumber(),
                    event.getStatus(),
                    "Automated update from Compliance Service"
            );
            log.info("Stock balance successfully updated for Batch: {}", event.getBatchNumber());
        } catch (Exception e) {
            log.error("Failed to update inventory for QA result — PartId: {}, Batch: {}, Error: {}",
                    event.getPartId(), event.getBatchNumber(), e.getMessage());
        }
    }
}