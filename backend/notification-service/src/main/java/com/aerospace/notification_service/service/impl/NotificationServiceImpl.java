package com.aerospace.notification_service.service.impl;

import com.aerospace.notification_service.entity.*;
import com.aerospace.notification_service.repository.NotificationRepository;
import com.aerospace.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.recipient:warehouse@aerospace.com}")
    private String recipient;

    @Override
    @Transactional
    public void sendLowStockAlert(Long partId, String partName, int currentQty, int reorderLevel) {
        String msg = String.format(
            "LOW STOCK ALERT — Part: %s (ID: %d) | Current: %d units | Reorder Level: %d | Action Required: Raise Purchase Order",
            partName, partId, currentQty, reorderLevel
        );
        deliver(partId, partName, null, NotificationType.LOW_STOCK, msg);
    }

    @Override
    @Transactional
    public void sendQAAlert(Long partId, String batchNumber, String status, String remarks, String inspector) {
        NotificationType type = "APPROVED".equals(status) ? NotificationType.QA_APPROVED : NotificationType.QA_REJECTED;
        String msg = String.format(
            "QA DECISION — Batch: %s | Part ID: %d | Decision: %s | Inspector: %s | Remarks: %s",
            batchNumber, partId, status, inspector != null ? inspector : "System", remarks
        );
        deliver(partId, null, batchNumber, type, msg);
    }

    @Override
    @Transactional
    public void sendMaterialIssuedAlert(Long partId, String batchNumber, int qty, String workOrderId) {
        String msg = String.format(
            "MATERIAL ISSUED — Part ID: %d | Batch: %s | Qty: %d | Work Order: %s",
            partId, batchNumber, qty, workOrderId != null ? workOrderId : "N/A"
        );
        deliver(partId, null, batchNumber, NotificationType.MATERIAL_ISSUED, msg);
    }

    @Override
    @Transactional
    public void sendStockReceivedAlert(Long partId, String partName, String batchNumber) {
        String msg = String.format(
            "STOCK RECEIVED — Part: %s (ID: %d) | Batch: %s | Status: PENDING_QA | QA inspection required",
            partName, partId, batchNumber != null ? batchNumber : "N/A"
        );
        deliver(partId, partName, batchNumber, NotificationType.STOCK_RECEIVED, msg);
    }

    @Override
    public List<NotificationLog> getRecent() {
        return notificationRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @Override
    public List<NotificationLog> getByPartId(Long partId) {
        return notificationRepository.findByPartIdOrderByCreatedAtDesc(partId);
    }

    // Core delivery method — logs to DB + console always, email only if enabled
    private void deliver(Long partId, String partName, String batchNumber,
                         NotificationType type, String message) {
        // Always log to console — useful during development
        log.info("NOTIFICATION [{}] → {}", type, message);

        NotificationLog logEntry = NotificationLog.builder()
                .type(type)
                .partId(partId)
                .partName(partName)
                .batchNumber(batchNumber)
                .message(message)
                .recipient(recipient)
                .status(NotificationStatus.PENDING)
                .build();

        if (emailEnabled) {
            try {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(recipient);
                mail.setSubject("[Aerospace ERP] " + type.name().replace("_", " "));
                mail.setText(message + "\n\nTimestamp: " + LocalDateTime.now());
                mailSender.send(mail);

                logEntry.setStatus(NotificationStatus.SENT);
                logEntry.setSentAt(LocalDateTime.now());
                log.info("Email sent to: {}", recipient);
            } catch (Exception e) {
                logEntry.setStatus(NotificationStatus.FAILED);
                logEntry.setErrorMessage(e.getMessage());
                log.error("Email delivery failed: {}", e.getMessage());
            }
        } else {
            // Email disabled — mark as SENT since console log is the delivery
            logEntry.setStatus(NotificationStatus.SENT);
            logEntry.setSentAt(LocalDateTime.now());
        }

        notificationRepository.save(logEntry);
    }
}