package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class QAStatusChangedEvent { 
    private Long partId; 
    private String batchNumber; 
    private String status; 
}
