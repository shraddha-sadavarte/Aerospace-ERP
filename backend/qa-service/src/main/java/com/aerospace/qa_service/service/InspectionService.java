package com.aerospace.qa_service.service;

import com.aerospace.qa_service.dto.InspectionRequestDTO;
import com.aerospace.qa_service.dto.InspectionResponseDTO;

import java.util.List;

public interface InspectionService {

    // Core: QA engineer submits a decision → saves to DB → publishes to Kafka
    InspectionResponseDTO submitDecision(InspectionRequestDTO dto);

    // Get full history for one part
    List<InspectionResponseDTO> getByPartId(Long partId);

    // Get full history for one batch — traceability
    List<InspectionResponseDTO> getByBatchNumber(String batchNumber);

    // Get one inspection by its ID
    InspectionResponseDTO getById(Long id);

    // Retry publishing failed Kafka messages
    void retryFailedPublications();
}