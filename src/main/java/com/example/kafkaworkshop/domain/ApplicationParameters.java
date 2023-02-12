package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class ApplicationParameters implements Comparable<ApplicationParameters> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonProperty(value = "worker", required = true)
    private WorkerParameters workerParameters;

    @JsonProperty(value = "optimiser", required = true)
    private OptimiserParameters optimiserParameters;

    public WorkerParameters getWorkerParameters() {
        return workerParameters;
    }

    public OptimiserParameters getOptimiserParameters() {
        return optimiserParameters;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(ApplicationParameters other) {
        return other.createdAt.compareTo(this.createdAt);
    }
}
