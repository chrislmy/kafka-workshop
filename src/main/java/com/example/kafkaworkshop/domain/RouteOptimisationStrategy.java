package com.example.kafkaworkshop.domain;

public enum RouteOptimisationStrategy {

    GREEDY,
    SIMULATED_ANNEALING,
    MULTI_LAYER_SIMULATED_ANNEALING;

    public static RouteOptimisationStrategy toStrategy(String name) {
        switch (name) {
            case "greedy":
                return GREEDY;
            case "simulated_annealing":
                return SIMULATED_ANNEALING;
            default:
                return MULTI_LAYER_SIMULATED_ANNEALING;
        }
    }

    public String name(RouteOptimisationStrategy strategy) {
        switch (strategy) {
            case GREEDY:
                return "greedy";
            case SIMULATED_ANNEALING:
                return "simulated_annealing";
            default:
                return "multi_layer_simulated_annealing";
        }
    }
}
