package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartCreatedEvent {
    private Long id;
    private String name;
    private String serialNumber;
}