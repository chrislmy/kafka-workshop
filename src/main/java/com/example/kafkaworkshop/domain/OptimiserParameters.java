package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptimiserParameters {

    @JsonProperty(value = "temperature", required = true)
    private double temperature;

    @JsonProperty(value = "coolingFactor", required = true)
    private double coolingFactor;

    @JsonProperty(value = "subProblemSize", required = true)
    private int subProblemSize;

    public double getTemperature() {
        return temperature;
    }

    public double getCoolingFactor() {
        return coolingFactor;
    }

    public int getSubProblemSize() {
        return subProblemSize;
    }
}
