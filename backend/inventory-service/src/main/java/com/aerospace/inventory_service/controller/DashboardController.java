package com.aerospace.inventory_service.controller;

import com.aerospace.inventory_service.common.ApiResponse;
import com.aerospace.inventory_service.dto.DashboardResponseDTO;
import com.aerospace.inventory_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // ERP Dashboard: Real-time Stock Ledger & Balance overview
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardResponseDTO>> getDashboardStats() {
        DashboardResponseDTO stats = dashboardService.getDashboardStats();
        
        // This includes total approved stock, blocked QA stock, and rejected items
        return ResponseEntity.ok(new ApiResponse<>("ERP_STATS_RETRIEVED", stats));
    }
}
