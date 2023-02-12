package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkerParameters {

    @JsonProperty(value = "routesProcessedPerBatch", required = true)
    private int routesProcessedPerBatch;

    @JsonProperty(value = "processingDelay", required = true)
    private long processingDelay;

    @JsonProperty(value = "optimisationStrategy", required = true)
    private String optimisationStrategy;

    public int getRoutesProcessedPerBatch() {
        return routesProcessedPerBatch;
    }

    public long getProcessingDelay() {
        return processingDelay;
    }

    public String getOptimisationStrategy() {
        return optimisationStrategy;
    }
}
