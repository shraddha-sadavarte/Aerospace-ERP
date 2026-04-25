package com.aerospace.qa_service.entity;

public enum QADecision {
    APPROVED,   // batch passes inspection — inventory moves to APPROVED status
    REJECTED    // batch fails inspection — inventory moves to REJECTED status
}