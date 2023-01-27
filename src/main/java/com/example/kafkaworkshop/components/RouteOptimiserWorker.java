package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.domain.RouteOptimisationStrategy;
import com.example.kafkaworkshop.repository.RouteRepository;
import com.example.kafkaworkshop.service.GreedyRouteOptimisationService;
import com.example.kafkaworkshop.service.RouteOptimisationService;
import com.example.kafkaworkshop.service.SimulatedAnnealingOptimisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteOptimiserWorker {

    private static final Logger logger = LoggerFactory.getLogger(RouteOptimiserWorker.class);
    private RouteRepository routeRepository;
    private GreedyRouteOptimisationService greedyRouteOptimisationService;
    private SimulatedAnnealingOptimisationService simulatedAnnealingOptimisationService;
    private OptimisedRouteReporter optimisedRouteReporter;

    @Value("${application.workers.route-optimiser.routes-processed-per-batch}")
    private int routesProcessedPerBatch;
    @Value("${application.workers.route-optimiser.processing-delay}")
    private long processingDelay;

    @Value("${application.workers.route-optimiser.optimisation-strategy}")
    private String optimisationStrategy;

    public RouteOptimiserWorker(RouteRepository routeRepository,
            GreedyRouteOptimisationService greedyRouteOptimisationService,
            SimulatedAnnealingOptimisationService simulatedAnnealingOptimisationService,
            OptimisedRouteReporter optimisedRouteReporter) {
        this.routeRepository = routeRepository;
        this.greedyRouteOptimisationService = greedyRouteOptimisationService;
        this.simulatedAnnealingOptimisationService = simulatedAnnealingOptimisationService;
        this.optimisedRouteReporter = optimisedRouteReporter;
    }

    public void start() throws InterruptedException {
        RouteOptimisationStrategy strategy = RouteOptimisationStrategy.toStrategy(optimisationStrategy);
        RouteOptimisationService routeOptimisationService = switch (strategy) {
            case GREEDY -> greedyRouteOptimisationService;
            default -> simulatedAnnealingOptimisationService;
        };

        while(true) {
            List<Route> routes = routeRepository.getBatchOfRoutes(routesProcessedPerBatch);
            try {
                ExecutionTimer.TimedResult<List<Route>> timedResult = new ExecutionTimer().timeCheckedFunction(routes,
                        routeOptimisationService::getOptimisedRoutes);
                optimisedRouteReporter.report(routes, timedResult.result, timedResult.elapsed);
                Thread.sleep(processingDelay);
            } catch (Exception e) {
                logger.error("Failed to optimise batch of routes: {}", e.getMessage());
                routeRepository.addRoutes(routes);
            }
        }
    }
}
