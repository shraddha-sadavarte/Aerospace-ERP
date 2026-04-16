package com.aerospace.inventory_service.mapper;

import com.aerospace.inventory_service.dto.*;
import com.aerospace.inventory_service.entity.Part;
import org.springframework.stereotype.Component;

@Component
public class PartMapper {

    public Part toEntity(PartRequestDTO dto) {
        return Part.builder()
                .name(dto.getName())
                .serialNumber(dto.getSerialNumber())
                .batchNumber(dto.getBatchNumber())
                .quantity(dto.getQuantity())
                .location(dto.getLocation())
                .partType(dto.getPartType())
                .criticality(dto.getCriticality())
                .reorderLevel(dto.getReorderLevel())
                .build();
    }

    public PartResponseDTO toDTO(Part part) {
        return PartResponseDTO.builder()
                .id(part.getId())
                .name(part.getName())
                .serialNumber(part.getSerialNumber())
                .quantity(part.getQuantity())
                .location(part.getLocation())
                .build();
    }
}