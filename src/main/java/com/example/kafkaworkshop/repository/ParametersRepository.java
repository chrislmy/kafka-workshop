package com.example.kafkaworkshop.repository;

import com.example.kafkaworkshop.domain.ApplicationParameters;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.PriorityQueue;

@Component
public class ParametersRepository {

    private PriorityQueue<ApplicationParameters> parameters;

    public ParametersRepository() {
        this.parameters = new PriorityQueue<>();
    }

    public void addParameters(ApplicationParameters parameters) {
        this.parameters.offer(parameters);
    }

    public Optional<ApplicationParameters> getLatest() {
        if (parameters.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(parameters.peek());
    }
}
