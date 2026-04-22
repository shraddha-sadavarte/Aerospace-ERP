package com.aerospace.inventory_service.service.impl;

import com.aerospace.inventory_service.dto.*;
import com.aerospace.inventory_service.entity.*;
import com.aerospace.inventory_service.exception.ResourceNotFoundException;
import com.aerospace.inventory_service.mapper.PartMapper;
import com.aerospace.inventory_service.repository.*;
import com.aerospace.inventory_service.service.PartService;
import com.aerospace.inventory_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final StockBalanceRepository balanceRepository;
    private final TransactionService transactionService;
    private final PartMapper mapper;
    private final UomConversionRepository uomRepository;

    @Override
    @Transactional
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public PartResponseDTO createPart(PartRequestDTO dto) {
        if (partRepository.findBySkuCode(dto.getSerialNumber()).isPresent()) {
            throw new RuntimeException("Part with this SKU/Serial already exists");
        }
        Part part = mapper.toEntity(dto);
        return mapper.toDTO(partRepository.save(part));
    }

    @Override
    public PartResponseDTO getById(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + id));
        return mapWithLiveStock(part, StockStatus.APPROVED);
    }

    // expiryDate is saved onto the StockBalance row here.
    // ExpiryScheduler queries: findAllByExpiryDateBeforeAndStatus(now, APPROVED)
    // and auto-rejects those rows — it needs this value to exist in the DB.
    @Override
    @Transactional
    public PartResponseDTO addStock(Long id, int quantity, String batch, String loc, LocalDateTime expiryDate) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        StockBalance balance = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(id, batch, StockStatus.PENDING_QA)
                .orElse(StockBalance.builder()
                        .partId(id).batchNumber(batch)
                        .status(StockStatus.PENDING_QA).quantity(0).build());

        balance.setQuantity(balance.getQuantity() + quantity);
        balance.setLocation(loc);
        balance.setExpiryDate(expiryDate); // persisted to DB — scheduler reads this
        balanceRepository.save(balance);

        transactionService.logTransaction(id, batch, "GRN_RECEIPT", quantity, "Inward to PENDING_QA");

        PartResponseDTO response = mapper.toDTO(part);
        response.setQuantity(balance.getQuantity());
        response.setBatchNumber(batch);
        response.setLocation(loc);
        response.setExpiryDate(expiryDate); // included in response so frontend can display immediately
        response.setStatus(StockStatus.PENDING_QA.name());
        return response;
    }

    @Override
    @Transactional
    public PartResponseDTO issueToProduction(Long id, int quantity, String batch) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        StockBalance balance = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(id, batch, StockStatus.APPROVED)
                .orElseThrow(() -> new RuntimeException("No APPROVED stock for batch: " + batch));

        if (balance.getQuantity() < quantity) throw new RuntimeException("Insufficient approved stock");

        balance.setQuantity(balance.getQuantity() - quantity);
        balanceRepository.save(balance);

        transactionService.logTransaction(id, batch, "PRODUCTION_ISSUE", -quantity, "Material Issued");

        PartResponseDTO response = mapper.toDTO(part);
        response.setQuantity(balance.getQuantity());
        response.setBatchNumber(batch);
        response.setStatus(StockStatus.APPROVED.name());
        return response;
    }

    @Override
    @Transactional
    public PartResponseDTO processQADecision(Long id, String batch, String newStatus, String remarks) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        StockBalance pending = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(id, batch, StockStatus.PENDING_QA)
                .orElseThrow(() -> new ResourceNotFoundException("Pending stock not found for batch: " + batch));

        int qty = pending.getQuantity();
        String loc = pending.getLocation();
        LocalDateTime expiry = pending.getExpiryDate(); // carry expiry date forward to next status
        balanceRepository.delete(pending);

        StockBalance target = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(id, batch, StockStatus.valueOf(newStatus))
                .orElse(StockBalance.builder()
                        .partId(id).batchNumber(batch)
                        .status(StockStatus.valueOf(newStatus)).quantity(0).build());

        target.setQuantity(target.getQuantity() + qty);
        target.setLocation(loc);
        target.setExpiryDate(expiry); // carry expiry to APPROVED/REJECTED row — scheduler still needs it
        balanceRepository.save(target);

        transactionService.logTransaction(id, batch, "QA_DECISION", 0,
                "Moved to " + newStatus + ": " + remarks);

        PartResponseDTO response = mapper.toDTO(part);
        response.setQuantity(target.getQuantity());
        response.setBatchNumber(batch);
        response.setStatus(newStatus);
        response.setQaRemarks(remarks);
        response.setExpiryDate(expiry);
        return response;
    }

    @Override
    public List<PartResponseDTO> getAllParts() {
        return partRepository.findAll().stream().map(p -> mapWithLiveStock(p, null)).toList();
    }

    @Override
    public Page<PartResponseDTO> getAllParts(Pageable pageable) {
        return partRepository.findAll(pageable).map(p -> mapWithLiveStock(p, null));
    }

    @Override
    public List<PartResponseDTO> searchByName(String name) {
        return partRepository.findByNameContainingIgnoreCase(name).stream()
                .map(p -> mapWithLiveStock(p, null)).toList();
    }

    @Override
    public List<InventoryTransaction> getTransactionHistory(Long partId) {
        return transactionService.getByPartId(partId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public PartResponseDTO updatePart(Long id, PartRequestDTO dto) {
        log.info("ERP: Updating material master for id: {}", id);
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        part.setName(dto.getName());
        part.setPartType(dto.getPartType());
        part.setCriticality(dto.getCriticality());
        part.setReorderLevel(dto.getReorderLevel());
        part.setSkuCode(dto.getSerialNumber());

        return mapWithLiveStock(partRepository.save(part), null);
    }

    @Override
    @Transactional
    public void reserveStock(Long partId, String batch, int quantity) {
        StockBalance approvedStock = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(partId, batch, StockStatus.APPROVED)
                .orElseThrow(() -> new RuntimeException("Approved stock not found"));

        if (approvedStock.getQuantity() < quantity)
            throw new RuntimeException("Insufficient stock to reserve");

        approvedStock.setQuantity(approvedStock.getQuantity() - quantity);
        balanceRepository.save(approvedStock);

        StockBalance reservedStock = balanceRepository
                .findByPartIdAndBatchNumberAndStatus(partId, batch, StockStatus.RESERVED)
                .orElse(StockBalance.builder()
                        .partId(partId).batchNumber(batch)
                        .status(StockStatus.RESERVED).quantity(0).build());

        // Carry expiry date from APPROVED → RESERVED so scheduler still tracks it
        reservedStock.setExpiryDate(approvedStock.getExpiryDate());
        reservedStock.setQuantity(reservedStock.getQuantity() + quantity);
        balanceRepository.save(reservedStock);

        transactionService.logTransaction(partId, batch, "STOCK_RESERVATION",
                quantity, "Reserved for Work Order");
    }

    @Override
    @Transactional
    public PartResponseDTO addStockWithConversion(Long id, int inputQty, String inputUom,
                                                   String batch, String loc) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        double factor = 1.0;
        if (!part.getUom().equals(inputUom)) {
            UomConversion conv = uomRepository.findByFromUomAndToUom(inputUom, part.getUom())
                    .orElseThrow(() -> new RuntimeException("No UOM conversion found: "
                            + inputUom + " -> " + part.getUom()));
            factor = conv.getFactor();
        }

        // null expiryDate — caller should use addStock() directly if expiry is known
        return addStock(id, (int) (inputQty * factor), batch, loc, null);
    }

    @Override
    @Transactional
    public void transferStock(Long id, String batch, String fromBin, String toBin, int qty) {
        StockBalance source = balanceRepository
                .findByPartIdAndBatchNumberAndLocation(id, batch, fromBin)
                .orElseThrow(() -> new RuntimeException("Source bin location not found"));

        if (source.getQuantity() < qty) throw new RuntimeException("Not enough stock in source bin");

        source.setQuantity(source.getQuantity() - qty);
        balanceRepository.save(source);

        StockBalance target = balanceRepository
                .findByPartIdAndBatchNumberAndLocation(id, batch, toBin)
                .orElse(StockBalance.builder()
                        .partId(id).batchNumber(batch)
                        .location(toBin).status(source.getStatus()).quantity(0).build());

        target.setQuantity(target.getQuantity() + qty);
        target.setExpiryDate(source.getExpiryDate()); // carry expiry date to new bin
        balanceRepository.save(target);

        transactionService.logTransaction(id, batch, "LOCATION_TRANSFER",
                qty, "Moved from " + fromBin + " to " + toBin);
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    private PartResponseDTO mapWithLiveStock(Part part, StockStatus status) {
        PartResponseDTO dto = mapper.toDTO(part);

        List<StockBalance> balances = balanceRepository.findAllBalancesByPart(part.getId());

        long totalQty = balances.stream()
                .filter(b -> status == null || b.getStatus() == status)
                .mapToLong(b -> b.getQuantity() != null ? b.getQuantity() : 0L)
                .sum();
        dto.setQuantity((int) totalQty);

        if (!balances.isEmpty()) {
            StockBalance primaryBalance = balances.get(0);
            dto.setBatchNumber(primaryBalance.getBatchNumber());
            dto.setStatus(primaryBalance.getStatus().name());
            dto.setLocation(primaryBalance.getLocation());
            dto.setExpiryDate(primaryBalance.getExpiryDate()); // frontend reads this for the expiry badge
        } else {
            dto.setStatus("EMPTY");
        }

        return dto;
    }
}