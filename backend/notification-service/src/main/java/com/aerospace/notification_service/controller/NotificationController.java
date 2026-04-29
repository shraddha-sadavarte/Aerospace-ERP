package com.aerospace.notification_service.controller;

import com.aerospace.notification_service.common.ApiResponse;
import com.aerospace.notification_service.entity.NotificationLog;
import com.aerospace.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/v1/notifications/recent — last 20 notifications across all types
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<NotificationLog>>> getRecent() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", notificationService.getRecent()));
    }

    // GET /api/v1/notifications/part/5 — all notifications for one part
    @GetMapping("/part/{partId}")
    public ResponseEntity<ApiResponse<List<NotificationLog>>> getByPart(@PathVariable Long partId) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", notificationService.getByPartId(partId)));
    }
}