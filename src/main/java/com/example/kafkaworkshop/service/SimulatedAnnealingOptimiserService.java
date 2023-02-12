package com.example.kafkaworkshop.service;

import com.example.kafkaworkshop.components.SimulatedAnnealingOptimiser;
import com.example.kafkaworkshop.domain.ApplicationParameters;
import com.example.kafkaworkshop.domain.OptimiserParameters;
import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.repository.ParametersRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SimulatedAnnealingOptimiserService implements RouteOptimisationService {

    private SimulatedAnnealingOptimiser optimiser;
    private ParametersRepository parametersRepository;

    public SimulatedAnnealingOptimiserService(SimulatedAnnealingOptimiser optimiser, ParametersRepository parametersRepository) {
        this.optimiser = optimiser;
        this.parametersRepository = parametersRepository;
    }

    @Override
    public List<Route> getOptimisedRoutes(List<Route> routes) {
        Optional<ApplicationParameters> params = parametersRepository.getLatest();

        params.ifPresent((parameters -> {
            OptimiserParameters optimiserParameters = parameters.getOptimiserParameters();
            optimiser.setParameters(optimiserParameters);
        }));

        return routes.stream()
            .map(optimiser::optimiseRoute)
            .collect(Collectors.toList());
    }
}
