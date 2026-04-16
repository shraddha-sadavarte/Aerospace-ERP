package com.aerospace.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartResponseDTO {
    private Long id;
    private String name;
    private String serialNumber;
    private Integer quantity;
    private String location;
}