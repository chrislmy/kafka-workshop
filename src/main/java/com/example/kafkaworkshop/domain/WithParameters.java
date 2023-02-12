package com.example.kafkaworkshop.domain;

public interface WithParameters<ParameterType> {

    void setParameters(ParameterType params);
}
