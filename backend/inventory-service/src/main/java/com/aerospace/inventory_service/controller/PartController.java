package com.aerospace.inventory_service.controller;

import com.aerospace.inventory_service.dto.PartRequestDTO;
import com.aerospace.inventory_service.dto.PartResponseDTO;
import com.aerospace.inventory_service.service.PartService;
import com.aerospace.inventory_service.common.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor   
public class PartController {

    private final PartService partService;  

    @PostMapping
    public ResponseEntity<ApiResponse<PartResponseDTO>> create(@RequestBody PartRequestDTO dto) {

        PartResponseDTO response = partService.createPart(dto);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartResponseDTO>>> getAll() {

        List<PartResponseDTO> list = partService.getAllParts();

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", list)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartResponseDTO>> getById(@PathVariable Long id) {

        PartResponseDTO response = partService.getById(id);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", response)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PartResponseDTO>> update(@PathVariable Long id, @RequestBody PartRequestDTO dto){
        return ResponseEntity.ok(
            new ApiResponse<>("UPDATED", partService.updatedPart(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

        partService.deletePart(id);

        return ResponseEntity.ok(
            new ApiResponse<>("DELETED", "Part removed successfully")
        );
    }

    @PostMapping("/{id}/add-stock")
    public ResponseEntity<ApiResponse<PartResponseDTO>> addStock(
            @PathVariable Long id,
            @RequestParam int qty) {

        return ResponseEntity.ok(
            new ApiResponse<>("STOCK_ADDED", partService.addStock(id, qty))
        );
    }

    @PostMapping("/{id}/consume")
    public ResponseEntity<ApiResponse<PartResponseDTO>> consumeStock(
            @PathVariable Long id,
            @RequestParam int qty) {

        return ResponseEntity.ok(
            new ApiResponse<>("STOCK_CONSUMED", partService.consumeStock(id, qty))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<list<PartResponseDTO>>> search(
            @RequestParam String name){
                return ResponseEntity.ok(
                    new ApiResponse<>("SUCCESS", partService.searchByName(name))
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PartResponseDTO>>> getAll(Pageable pageable) {

        return ResponseEntity.ok(
            new ApiResponse<>("SUCCESS", partService.getAllParts(pageable))
        );
    }
    
}