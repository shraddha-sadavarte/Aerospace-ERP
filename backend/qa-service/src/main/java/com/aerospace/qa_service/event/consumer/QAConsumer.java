package com.aerospace.qa_service.event.consumer;

import com.aerospace.qa_service.event.QAKafkaTopics;
import com.aerospace.qa_service.event.StockReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QAConsumer {

    // Listens for inventory.created — fired when inventory receives a GRN
    // In a full system this would create a QA inspection task/ticket automatically
    // For now it logs the pending inspection so QA engineers know what to inspect
    @KafkaListener(topics = QAKafkaTopics.INVENTORY_STOCK_RECEIVED, groupId = "qa-group")
    public void onStockReceived(StockReceivedEvent event) {
        log.info("QA QUEUE: New batch pending inspection — Part: '{}' (ID: {}), SKU: {}",
                 event.getName(), event.getId(), event.getSerialNumber());

        // Future enhancement: auto-create InspectionTask entity here
        // so QA engineers see a pending queue in the QA dashboard
    }
}