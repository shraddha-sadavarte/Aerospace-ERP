package com.aerospace.inventory_service.service.impl;

import com.aerospace.inventory_service.entity.InventoryTransaction;
import com.aerospace.inventory_service.repository.TransactionRepository;
import com.aerospace.inventory_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void logTransaction(Long partId, String type, int quantity) {
        InventoryTransaction transaction = InventoryTransaction.builder()
                .partId(partId)
                .type(type)
                .quantity(quantity)
                .timestamp(LocalDateTime.now())
                .build();
        
        transactionRepository.save(transaction);
    }
}
