package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartCreatedEvent {
    private Long id;
    private String name;
    private String serialNumber;
}