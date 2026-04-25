package com.aerospace.qa_service.repository;

import com.aerospace.qa_service.entity.InspectionReport;
import com.aerospace.qa_service.entity.QADecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<InspectionReport, Long> {

    // All inspections for a specific part — full audit trail
    List<InspectionReport> findByPartIdOrderByInspectedAtDesc(Long partId);

    // All inspections for a specific batch — traceability
    List<InspectionReport> findByBatchNumberOrderByInspectedAtDesc(String batchNumber);

    // All pending (not yet published to inventory) — for retry mechanism
    List<InspectionReport> findByPublishedToInventoryFalse();

    // Count decisions by type — for dashboard
    long countByDecision(QADecision decision);

    // Check if a batch already has an inspection
    boolean existsByPartIdAndBatchNumber(Long partId, String batchNumber);
}