package com.aerospace.notification_service.service;

import com.aerospace.notification_service.entity.NotificationLog;
import com.aerospace.notification_service.entity.NotificationType;

import java.util.List;

public interface NotificationService {
    void sendLowStockAlert(Long partId, String partName, int currentQty, int reorderLevel);
    void sendQAAlert(Long partId, String batchNumber, String status, String remarks, String inspector);
    void sendMaterialIssuedAlert(Long partId, String batchNumber, int qty, String workOrderId);
    void sendStockReceivedAlert(Long partId, String partName, String batchNumber);
    List<NotificationLog> getRecent();
    List<NotificationLog> getByPartId(Long partId);
}