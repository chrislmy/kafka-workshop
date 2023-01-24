package com.example.kafkaworkshop.service;

import com.example.kafkaworkshop.components.GreedyRouteOptimiser;
import com.example.kafkaworkshop.domain.Route;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GreedyRouteOptimisationService implements RouteOptimisationService {

    private GreedyRouteOptimiser greedyRouteOptimiser;

    public GreedyRouteOptimisationService(GreedyRouteOptimiser greedyRouteOptimiser) {
        this.greedyRouteOptimiser = greedyRouteOptimiser;
    }

    @Override
    public List<Route> getOptimisedRoutes(List<Route> routes) {
        return routes
            .stream()
            .map(greedyRouteOptimiser::optimiseRoute)
            .collect(Collectors.toList());
    }
}
