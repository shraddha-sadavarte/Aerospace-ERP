package com.aerospace.inventory_service.repository;

import com.aerospace.inventory_service.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    // Audit trail for a specific part
    List<InventoryTransaction> findByPartIdOrderByTimestampDesc(Long partId);
    
    // ERP Requirement: Audit trail for a specific batch (Traceability)
    List<InventoryTransaction> findByBatchNumberOrderByTimestampDesc(String batchNumber);
    
    // Filter by transaction type (e.g., all GRN receipts)
    List<InventoryTransaction> findByTransactionType(String transactionType);
}
