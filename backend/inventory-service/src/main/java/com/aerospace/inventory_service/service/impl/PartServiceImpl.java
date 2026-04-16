package com.aerospace.inventory_service.service.impl;

import com.aerospace.inventory_service.dto.*;
import com.aerospace.inventory_service.entity.Part;
import com.aerospace.inventory_service.event.PartCreatedEvent;
import com.aerospace.inventory_service.mapper.PartMapper;
import com.aerospace.inventory_service.repository.PartRepository;
import com.aerospace.inventory_service.service.PartService;
import com.aerospace.inventory_service.event.producer.InventoryProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartServiceImpl implements PartService {

    private final PartRepository repository;
    private final PartMapper mapper;
    private final InventoryProducer producer;

    @Override
    public PartResponseDTO createPart(PartRequestDTO dto) {

        log.info("Creating part with name: {}", dto.getName());

        if (repository.findBySerialNumber(dto.getSerialNumber()).isPresent()) {
            throw new RuntimeException("Serial already exists");
        }

        Part part = mapper.toEntity(dto);
        Part saved = repository.save(part);

        producer.sendCreatedEvent(
                new PartCreatedEvent(
                        saved.getId(),
                        saved.getName(),
                        saved.getSerialNumber()
                )
        );

        if (saved.getQuantity() < (saved.getReorderLevel() != null ? saved.getReorderLevel() : 0)) {
            log.warn("Low stock detected for {}", saved.getSerialNumber());
        }

        return mapper.toDTO(saved);
    }

    @Override
    public List<PartResponseDTO> getAllParts() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public PartResponseDTO getById(Long id) {
        Part part = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        return mapper.toDTO(part);
    }

    public PartResponseDTO updatePart(Long id, PartRequestDTO dto){
        Part part = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        part.setName(dto.getName());
        part.setQuantity(dto.getQuantity());
        part.setLocation(dto.getLocation());

        Part updated = repository.save(part);

        if(updated.getQuantity() < updated.getReorderLevel()) {
            producer.sendLowStockEvent(updated);
        }

        return mapper.toDTO(updated);
    }

    public void deletePart(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Part not found");
        }
        repository.deleteById(id);
    }

    public PartResponseDTO addStock(Long id, int quantity) {
        Part part = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        part.setQuantity(part.getQuantity() + quantity);

        return mapper.toDTO(repository.save(part));
    }

    public PartResponseDTO consumeStock(Long id, int quantity) {
        Part part = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        if (part.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        part.setQuantity(part.getQuantity() - quantity);

        Part updated = repository.save(part);
        if(updated.getQuantity() < updated.getReorderLevel()) {
            producer.sendLowStockEvent(updated);
        }

        return mapper.toDTO(repository.save(part));
    }

    //pagination
    public Page<PartResponseDTO> getAllParts(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toDTO);
    }
}