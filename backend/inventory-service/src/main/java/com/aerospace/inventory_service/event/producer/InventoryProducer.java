package com.aerospace.inventory_service.event.producer;

import com.aerospace.inventory_service.entity.Part;
import com.aerospace.inventory_service.event.KafkaTopics;
import com.aerospace.inventory_service.event.PartCreatedEvent;
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
        log.info("Sending event to Kafka: {}", event);
        kafkaTemplate.send(KafkaTopics.INVENTORY_CREATED, event);
    }

    public void sendLowStockEvent(Part updated) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendLowStockEvent'");
    }
}