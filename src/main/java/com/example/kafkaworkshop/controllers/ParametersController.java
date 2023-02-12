package com.example.kafkaworkshop.controllers;

import com.example.kafkaworkshop.domain.ApplicationParameters;
import com.example.kafkaworkshop.repository.ParametersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;

@RestController
public class ParametersController {

    private Clock clock;
    private ParametersRepository parametersRepository;

    public ParametersController(Clock clock, ParametersRepository parametersRepository) {
        this.clock = clock;
        this.parametersRepository = parametersRepository;
    }

    @PostMapping("/parameters")
    public ResponseEntity<ApplicationParameters> postParameters(@RequestBody ApplicationParameters parameters) {
        parameters.setCreatedAt(Instant.now(clock));
        parametersRepository.addParameters(parameters);
        return ResponseEntity.status(HttpStatus.CREATED).body(parameters);
    }
}
