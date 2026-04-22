package com.aerospace.inventory_service.event;

public class KafkaTopics {

    // Topics this service PUBLISHES to
    public static final String INVENTORY_CREATED  = "inventory.created";
    public static final String STOCK_LOW          = "inventory.stock.low";
    public static final String QA_STATUS_UPDATED  = "inventory.qa.status.updated";
    public static final String MATERIAL_ISSUED    = "inventory.material.issued";

    // Topics this service CONSUMES from other services
    public static final String COMPLIANCE_QA_RESULT = "compliance.qa.result";
}