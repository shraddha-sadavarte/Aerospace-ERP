package com.aerospace.inventory_service.service;

public interface TransactionService {
    void logTransaction(Long partId, String type, int quantity);
}
