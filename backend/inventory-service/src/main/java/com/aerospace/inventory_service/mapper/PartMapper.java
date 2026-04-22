package com.aerospace.inventory_service.mapper;

import com.aerospace.inventory_service.dto.*;
import com.aerospace.inventory_service.entity.Part;
import org.springframework.stereotype.Component;

@Component
public class PartMapper {

    public Part toEntity(PartRequestDTO dto) {
        if (dto == null) return null;

        return Part.builder()
                .name(dto.getName())
                // serialNumber from DTO maps to SKU/Code in ERP Entity
                .skuCode(dto.getSerialNumber()) 
                .partType(dto.getPartType())
                .criticality(dto.getCriticality())
                .reorderLevel(dto.getReorderLevel())
                // Removed quantity, batch, and status as they are now Ledger-based
                .build();
    }

    public PartResponseDTO toDTO(Part part) {
        if (part == null) return null;

        return PartResponseDTO.builder()
                .id(part.getId())
                .name(part.getName())
                .serialNumber(part.getSkuCode())
                .partType(part.getPartType())
                .criticality(part.getCriticality())
                .reorderLevel(part.getReorderLevel())
                // Note: quantity, status, and batchNumber will be set 
                // by the Service layer after aggregating StockBalances
                .build();
    }
}
