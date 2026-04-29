package com.aerospace.notification_service.entity;

public enum NotificationType {
    LOW_STOCK,          // inventory quantity fell below reorder level
    QA_APPROVED,        // batch moved to APPROVED status
    QA_REJECTED,        // batch moved to REJECTED status
    STOCK_RECEIVED,     // new GRN received, batch pending QA
    EXPIRY_WARNING,     // batch expiring within 30 days
    MATERIAL_ISSUED     // stock issued to production
}