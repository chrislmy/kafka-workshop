package com.example.kafkaworkshop.domain;

public enum RouteOptimisationStrategy {
    GREEDY,
    SIMULATED_ANNEALING;

    public static RouteOptimisationStrategy toStrategy(String name) {
        switch (name) {
            case "greedy":
                return GREEDY;
            default:
                return SIMULATED_ANNEALING;
        }
    }

    public String name(RouteOptimisationStrategy strategy) {
        switch (strategy) {
            case GREEDY:
                return "greedy";
            default:
                return "simulated_annealing";
        }
    }
}
