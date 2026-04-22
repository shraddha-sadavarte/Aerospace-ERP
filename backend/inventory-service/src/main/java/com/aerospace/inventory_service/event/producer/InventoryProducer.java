package com.aerospace.inventory_service.event.producer;

import com.aerospace.inventory_service.event.KafkaTopics;
import com.aerospace.inventory_service.event.MaterialIssuedEvent; // FIX 3: updated import
import com.aerospace.inventory_service.event.PartCreatedEvent;
import com.aerospace.inventory_service.event.QAStatusChangedEvent;
import com.aerospace.inventory_service.event.StockLowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreatedEvent(PartCreatedEvent event) {
        log.info("Publishing INVENTORY_CREATED event for part: {}", event.getName());
        kafkaTemplate.send(KafkaTopics.INVENTORY_CREATED, event);
    }

    public void sendLowStockEvent(StockLowEvent event) {
        log.info("Publishing STOCK_LOW event for part ID: {}", event.getPartId());
        kafkaTemplate.send(KafkaTopics.STOCK_LOW, event);
    }

    public void sendMaterialIssuedEvent(MaterialIssuedEvent event) {
        log.info("Publishing MATERIAL_ISSUED event for part ID: {}, Work Order: {}",
                event.getMaterialId(), event.getWorkOrderId());
        kafkaTemplate.send(KafkaTopics.MATERIAL_ISSUED, event);
    }

    public void sendQAStatusChangedEvent(QAStatusChangedEvent event) {
        log.info("Publishing QA_STATUS_UPDATED event for part ID: {}", event.getPartId());
        kafkaTemplate.send(KafkaTopics.QA_STATUS_UPDATED, event);
    }

    // FIX 1: Removed the duplicate stub that threw UnsupportedOperationException
    // If you need to publish a QA event with just IDs (no full event object),
    // build the event object here and delegate to the method above:
    public void sendQAStatusChangedEvent(Long partId, String batchNumber, String status) {
        sendQAStatusChangedEvent(new QAStatusChangedEvent(partId, batchNumber, status));
    }
}