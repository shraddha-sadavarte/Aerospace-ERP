package com.aerospace.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Every notification sent is stored here — full audit trail
@Entity
@Table(name = "notification_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // What triggered this notification
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // Who/what it's about
    private Long partId;
    private String partName;
    private String batchNumber;

    // The message content
    @Column(nullable = false, length = 1000)
    private String message;

    // Delivery status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    private String recipient;
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}