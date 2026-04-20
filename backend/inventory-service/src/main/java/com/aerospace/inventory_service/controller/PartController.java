package com.aerospace.inventory_service.controller;

import com.aerospace.inventory_service.dto.PartRequestDTO;
import com.aerospace.inventory_service.dto.PartResponseDTO;
import com.aerospace.inventory_service.service.PartService;
import com.aerospace.inventory_service.common.ApiResponse;
import com.aerospace.inventory_service.entity.InventoryTransaction;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus; // Added for cleaner status codes
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class PartController {

        private final PartService partService;

        @PostMapping
        public ResponseEntity<ApiResponse<PartResponseDTO>> create(@RequestBody PartRequestDTO dto) {
                PartResponseDTO response = partService.createPart(dto);
                // Changed to .status(HttpStatus.CREATED) as it's best practice for POST
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ApiResponse<>("SUCCESS", response));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<PartResponseDTO>>> getAll() {
                List<PartResponseDTO> list = partService.getAllParts();
                return ResponseEntity.ok(
                                new ApiResponse<>("SUCCESS", list));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<PartResponseDTO>> getById(@PathVariable Long id) {
                PartResponseDTO response = partService.getById(id);
                return ResponseEntity.ok(
                                new ApiResponse<>("SUCCESS", response));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<PartResponseDTO>> update(@PathVariable Long id,
                        @RequestBody PartRequestDTO dto) {
                return ResponseEntity.ok(
                                new ApiResponse<>("UPDATED", partService.updatedPart(id, dto)));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
                partService.deletePart(id);
                return ResponseEntity.ok(
                                new ApiResponse<>("DELETED", "Part removed successfully"));
        }

        @PostMapping("/{id}/add-stock")
        public ResponseEntity<ApiResponse<PartResponseDTO>> addStock(
                        @PathVariable Long id,
                        @RequestParam int qty) {
                return ResponseEntity.ok(
                                new ApiResponse<>("STOCK_ADDED", partService.addStock(id, qty)));
        }

        @PostMapping("/{id}/consume")
        public ResponseEntity<ApiResponse<PartResponseDTO>> consumeStock(
                        @PathVariable Long id,
                        @RequestParam int qty) {
                return ResponseEntity.ok(
                                new ApiResponse<>("STOCK_CONSUMED", partService.consumeStock(id, qty)));
        }

        @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<PartResponseDTO>>> search(
                        @RequestParam String name) {
                // Removed the manual (List<PartResponseDTO>) cast to prevent potential
                // ClassCastException
                List<PartResponseDTO> results = partService.searchByName(name);
                return ResponseEntity.ok(
                                new ApiResponse<>("SUCCESS", results));
        }

        @GetMapping("/paged")
        public ResponseEntity<ApiResponse<Page<PartResponseDTO>>> getAll(Pageable pageable) {
                return ResponseEntity.ok(
                                new ApiResponse<Page<PartResponseDTO>>("SUCCESS", partService.getAllParts(pageable)));
        }

        @GetMapping("/{id}/transactions")
        public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getTransactionHistory(@PathVariable Long id) {
                List<InventoryTransaction> transactions = partService.getTransactionHistory(id);
                return ResponseEntity.ok(
                                new ApiResponse<>("SUCCESS", transactions));
        }

}
