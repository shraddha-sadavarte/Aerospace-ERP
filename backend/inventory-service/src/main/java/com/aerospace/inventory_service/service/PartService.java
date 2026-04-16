package com.aerospace.inventory_service.service;

import com.aerospace.inventory_service.dto.*;
import java.util.List;

public interface PartService {
    PartResponseDTO createPart(PartRequestDTO dto);
    List<PartResponseDTO> getAllParts();
    PartResponseDTO getById(Long id);
    PartResponseDTO updatedPart(Long id, PartRequestDTO dto);
    void deletePart(Long id);
    PartResponseDTO addStock(Long id, int quantity);
    PartResponseDTO consumeStock(Long id, int quantity);
    
}