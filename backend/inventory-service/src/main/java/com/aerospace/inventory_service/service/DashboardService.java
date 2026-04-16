package com.aerospace.inventory_service.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.aerospace.inventory_service.dto.DashboardResponseDTO;
import com.aerospace.inventory_service.repository.PartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PartRepository partRepository;

    @Cacheable("dashboardStats")
    public DashboardResponseDTO getDashboardStats(){
        //convert List<Object[]> from repo to Map for the DTO
        Map<String, Long> typeMap = partRepository.countPartsByType().stream()
            .collect(Collectors.toMap(
                array -> (String) array[0], 
                array -> (Long) array[1]
            ));

        return DashboardResponseDTO.builder()
            .totalParts(partRepository.count())
            .totalQuantity(Optional.ofNullable(partRepository.getTotalQuantity()).orElse(0L))
            .lowStockItems(partRepository.countLowStock())
            .outOfStockItems(partRepository.countOutOfStock())
            .partsByType(typeMap)
            .build();
    }
}
