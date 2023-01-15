package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthResponse {

    @JsonProperty("appId")
    private String applicationId;

    @JsonProperty("status")
    private String status;

    public HealthResponse(String applicationId, String status) {
        this.applicationId = applicationId;
        this.status = status;
    }
}