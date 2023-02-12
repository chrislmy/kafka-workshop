package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.domain.OptimiserParameters;
import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.domain.WithParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SimulatedAnnealingOptimiser implements RouteOptimiser, WithParameters<OptimiserParameters> {

    @Value("${application.route-optimiser.simulated-annealing.temperature}")
    private double temperature;

    @Value("${application.route-optimiser.simulated-annealing.cooling-factor}")
    private double coolingFactor;

    private Random random;

    public SimulatedAnnealingOptimiser(Random random) {
        this.random = random;
    }

    @Override
    public Route optimiseRoute(Route route) {
        double currentTemperature = temperature;
        Route currentRoute = route;
        Route bestRoute = route;

        while (currentTemperature > 1.0) {
            Route neighbour = currentRoute.clone();
            neighbour.randomiseRoute(random);

            double currentCost = currentRoute.cost();
            double neighbourCost = neighbour.cost();

            if (Math.random() < probability(currentCost, neighbourCost, currentTemperature)) {
                currentRoute = neighbour;
            }

            if (currentRoute.cost() < bestRoute.cost()) {
                bestRoute = currentRoute;
            }

            currentTemperature *= coolingFactor;
        }

        return bestRoute;
    }

    @Override
    public void setParameters(OptimiserParameters params) {
        this.temperature = params.getTemperature();
        this.coolingFactor = params.getCoolingFactor();
    }

    private double probability(double f1, double f2, double temp) {
        if (f2 < f1) return 1;
        return Math.exp((f1 - f2) / temp);
    }
}
