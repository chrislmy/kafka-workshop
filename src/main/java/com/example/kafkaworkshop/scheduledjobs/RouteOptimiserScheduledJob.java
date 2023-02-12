package com.example.kafkaworkshop.scheduledjobs;

import com.example.kafkaworkshop.components.ExecutionTimer;
import com.example.kafkaworkshop.components.OptimisedRouteReporter;
import com.example.kafkaworkshop.domain.*;
import com.example.kafkaworkshop.repository.ParametersRepository;
import com.example.kafkaworkshop.repository.RouteRepository;
import com.example.kafkaworkshop.service.GreedyRouteOptimisationService;
import com.example.kafkaworkshop.service.MultiLayerSimulatedAnnealingOptimisationService;
import com.example.kafkaworkshop.service.RouteOptimisationService;
import com.example.kafkaworkshop.service.SimulatedAnnealingOptimiserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RouteOptimiserScheduledJob implements WithParameters<WorkerParameters> {

    private static final Logger logger = LoggerFactory.getLogger(RouteOptimiserScheduledJob.class);

    private RouteRepository routeRepository;
    private ParametersRepository parametersRepository;
    private GreedyRouteOptimisationService greedyRouteOptimisationService;
    private MultiLayerSimulatedAnnealingOptimisationService multiLayerSimulatedAnnealingOptimisationService;
    private SimulatedAnnealingOptimiserService simulatedAnnealingOptimiserService;
    private OptimisedRouteReporter optimisedRouteReporter;

    @Value("${application.workers.route-optimiser.routes-processed-per-batch}")
    private int routesProcessedPerBatch;

    @Value("${application.workers.route-optimiser.optimisation-strategy}")
    private String optimisationStrategy;

    public RouteOptimiserScheduledJob(
            RouteRepository routeRepository,
            ParametersRepository parametersRepository,
            GreedyRouteOptimisationService greedyRouteOptimisationService,
            MultiLayerSimulatedAnnealingOptimisationService multiLayerSimulatedAnnealingOptimisationService,
            SimulatedAnnealingOptimiserService simulatedAnnealingOptimiserService,
            OptimisedRouteReporter optimisedRouteReporter
    ) {
        this.routeRepository = routeRepository;
        this.parametersRepository = parametersRepository;
        this.greedyRouteOptimisationService = greedyRouteOptimisationService;
        this.multiLayerSimulatedAnnealingOptimisationService = multiLayerSimulatedAnnealingOptimisationService;
        this.simulatedAnnealingOptimiserService = simulatedAnnealingOptimiserService;
        this.optimisedRouteReporter = optimisedRouteReporter;
    }

    public void optimiseRoutes() {
        Optional<ApplicationParameters> params = parametersRepository.getLatest();
        params.ifPresent((parameters -> setParameters(parameters.getWorkerParameters())));

        RouteOptimisationStrategy strategy = RouteOptimisationStrategy.toStrategy(optimisationStrategy);
        logger.info("[Worker]: Starting route optimisation process with strategy: {}", strategy.name());
        RouteOptimisationService routeOptimisationService = switch (strategy) {
            case GREEDY -> greedyRouteOptimisationService;
            case SIMULATED_ANNEALING -> simulatedAnnealingOptimiserService;
            default -> multiLayerSimulatedAnnealingOptimisationService;
        };

        List<Route> routes = routeRepository.getBatchOfRoutes(routesProcessedPerBatch);
        try {
            ExecutionTimer.TimedResult<List<Route>> timedResult = new ExecutionTimer().timeCheckedFunction(routes,
                    routeOptimisationService::getOptimisedRoutes);
            optimisedRouteReporter.report(routes, timedResult.result, timedResult.elapsed);
        } catch (Exception e) {
            logger.error("Failed to optimise batch of routes: {}", e.getMessage());
            routeRepository.addRoutes(routes);
        }
    }

    @Override
    public void setParameters(WorkerParameters params) {
        this.routesProcessedPerBatch = params.getRoutesProcessedPerBatch();
        this.optimisationStrategy = params.getOptimisationStrategy();
    }
}
