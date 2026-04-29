package com.aerospace.notification_service.repository;

import com.aerospace.notification_service.entity.NotificationLog;
import com.aerospace.notification_service.entity.NotificationStatus;
import com.aerospace.notification_service.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByPartIdOrderByCreatedAtDesc(Long partId);
    List<NotificationLog> findByStatusOrderByCreatedAtDesc(NotificationStatus status);
    List<NotificationLog> findByTypeOrderByCreatedAtDesc(NotificationType type);
    List<NotificationLog> findTop20ByOrderByCreatedAtDesc();
}