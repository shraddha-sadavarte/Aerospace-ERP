package com.aerospace.inventory_service.service;

import com.aerospace.inventory_service.dto.*;
import com.aerospace.inventory_service.entity.InventoryTransaction;

import java.util.List;

import org.springframework.data.domain.Page; // Added for Page support
import org.springframework.data.domain.Pageable;

public interface PartService {
    PartResponseDTO createPart(PartRequestDTO dto);
    List<PartResponseDTO> getAllParts();
    PartResponseDTO getById(Long id);
    PartResponseDTO updatedPart(Long id, PartRequestDTO dto);
    void deletePart(Long id);
    PartResponseDTO addStock(Long id, int quantity);
    PartResponseDTO consumeStock(Long id, int quantity);
    
    // Updated from Object to List<PartResponseDTO> to match Controller expectations
    List<PartResponseDTO> searchByName(String name);
    
    // Updated from Object to Page<PartResponseDTO> for proper pagination support
    Page<PartResponseDTO> getAllParts(Pageable pageable);

    List<InventoryTransaction> getTransactionHistory(Long partId);
}
