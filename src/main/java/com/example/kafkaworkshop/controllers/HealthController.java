package com.example.kafkaworkshop.controllers;

import com.example.kafkaworkshop.domain.HealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private static final String RESPONSE_BODY = "healthy";

    @Value("${application.app-id}")
    private String applicationId;

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        HealthResponse response = new HealthResponse(applicationId, RESPONSE_BODY);
        return ResponseEntity.ok(response);
    }
}
