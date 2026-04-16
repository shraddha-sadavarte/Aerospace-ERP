package com.aerospace.inventory_service.event.consumer;

import com.aerospace.inventory_service.event.KafkaTopics;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryConsumer {

    @KafkaListener(topics = KafkaTopics.INVENTORY_CREATED, groupId = "inventory-group")
    public void consume(String message) {
        log.info("Received event: {}", message);
    }
}