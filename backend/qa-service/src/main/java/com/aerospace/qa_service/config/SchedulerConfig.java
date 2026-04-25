package com.aerospace.qa_service.config;

import com.aerospace.qa_service.service.InspectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final InspectionService inspectionService;

    // Every 5 minutes, retry any QA decisions that failed to publish to Kafka
    // This ensures no decision is lost if Kafka was temporarily down
    @Scheduled(fixedDelay = 300000)
    public void retryFailedKafkaPublications() {
        log.debug("Scheduler: checking for unpublished QA decisions...");
        inspectionService.retryFailedPublications();
    }
}