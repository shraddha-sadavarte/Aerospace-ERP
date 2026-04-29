package com.aerospace.notification_service.event.consumer;

import com.aerospace.notification_service.event.*;
import com.aerospace.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    // Listens for low stock — inventory publishes this when qty < reorderLevel
    // Triggers: raise PO, alert store manager
    @KafkaListener(topics = NotificationTopics.STOCK_LOW, groupId = "notification-group",
                   containerFactory = "stockLowListenerFactory")
    public void onLowStock(@Payload StockLowEvent event) {
        log.info("Low stock event received — Part: {}, Qty: {}", event.getPartName(), event.getCurrentQty());
        notificationService.sendLowStockAlert(
            event.getPartId(),
            event.getPartName(),
            event.getCurrentQty(),
            event.getReorderLevel() != null ? event.getReorderLevel() : 0
        );
    }

    // Listens for QA decisions — fires after inventory processes compliance.qa.result
    // Triggers: notify QA team, procurement if rejected
    @KafkaListener(topics = NotificationTopics.QA_STATUS_UPDATED, groupId = "notification-group",
                   containerFactory = "qaStatusListenerFactory")
    public void onQAStatusChanged(@Payload QAStatusChangedEvent event) {
        log.info("QA status event — Batch: {}, Status: {}", event.getBatchNumber(), event.getStatus());
        notificationService.sendQAAlert(
            event.getPartId(),
            event.getBatchNumber(),
            event.getStatus(),
            event.getRemarks(),
            event.getInspectorName()
        );
    }

    // Listens for material issued — inventory publishes after issueToProduction()
    // Triggers: notify production floor, finance for costing
    @KafkaListener(topics = NotificationTopics.MATERIAL_ISSUED, groupId = "notification-group",
                   containerFactory = "materialIssuedListenerFactory")
    public void onMaterialIssued(@Payload MaterialIssuedEvent event) {
        log.info("Material issued event — Batch: {}, Qty: {}", event.getBatchNumber(), event.getQuantity());
        notificationService.sendMaterialIssuedAlert(
            event.getMaterialId(),
            event.getBatchNumber(),
            event.getQuantity() != null ? event.getQuantity() : 0,
            event.getWorkOrderId()
        );
    }

    // Listens for new parts created — inventory publishes on GRN receipt
    // Triggers: notify QA team that a new batch needs inspection
    @KafkaListener(topics = NotificationTopics.STOCK_RECEIVED, groupId = "notification-group",
                   containerFactory = "stockReceivedListenerFactory")
    public void onStockReceived(@Payload StockReceivedEvent event) {
        log.info("Stock received event — Part: {}", event.getName());
        notificationService.sendStockReceivedAlert(
            event.getId(),
            event.getName(),
            null // batch number not in PartCreatedEvent — notify on part level
        );
    }
}