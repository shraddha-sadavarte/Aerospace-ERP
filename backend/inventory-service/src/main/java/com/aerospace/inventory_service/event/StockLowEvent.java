package com.aerospace.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class StockLowEvent { 
    private Long partId; 
    private String partName; 
    private Integer currentQty; 
}
