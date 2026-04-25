package com.aerospace.qa_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// What the QA engineer submits via the API
@Data
public class InspectionRequestDTO {

    @NotNull(message = "Part ID is required")
    private Long partId;

    @NotBlank(message = "Batch number is required")
    private String batchNumber;

    @NotNull(message = "Decision is required — APPROVED or REJECTED")
    private String decision;  // "APPROVED" or "REJECTED"

    @NotBlank(message = "Remarks are mandatory for aerospace traceability")
    private String remarks;

    // Optional fields
    private String certificateNumber;  // e.g. "CERT-2024-00123"
    private String inspectorName;
    private String location;           // needed so inventory knows which bin to update
}