package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.consumers.RouteConsumer;
import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.repository.RouteRepository;
import com.example.kafkaworkshop.service.RouteOptimisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RouteOptimiserWorker {

    private static final Logger logger = LoggerFactory.getLogger(RouteConsumer.class);

    private RouteRepository routeRepository;
    private RouteOptimisationService routeOptimisationService;

    private HaversineDistanceCalculator distanceCalculator;

    @Value("${application.workers.route-optimiser.routes-processed-per-batch}")
    private int routesProcessedPerBatch;
    @Value("${application.workers.route-optimiser.processing-delay}")
    private long processingDelay;

    public RouteOptimiserWorker(RouteRepository routeRepository, RouteOptimisationService routeOptimisationService,
                                HaversineDistanceCalculator distanceCalculator) {
        this.routeRepository = routeRepository;
        this.routeOptimisationService = routeOptimisationService;
        this.distanceCalculator = distanceCalculator;
    }

    public void start() throws InterruptedException {
        while(true) {
            List<Route> routes = routeRepository.getBatchOfRoutes(routesProcessedPerBatch);
            Map<String, Route> routesMap = routes
                    .stream()
                    .collect(Collectors.toMap(Route::getRouteId, Function.identity()));
            List<Route> optimisedRoutes = routeOptimisationService.getOptimisedRoutes(routes);
            for (Route route : optimisedRoutes) {
                logger.info("[Worker]: Cost route id: {}, before: {}, after: {}",
                        route.getRouteId(), routesMap.get(route.getRouteId()).cost(distanceCalculator),
                        route.cost(distanceCalculator));
            }
            Thread.sleep(processingDelay);
        }
    }
}
