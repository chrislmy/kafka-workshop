package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.domain.RouteOptimisationStrategy;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OptimisedRouteReporter {

    private static final Logger logger = LoggerFactory.getLogger(OptimisedRouteReporter.class);
    private static final String COST_IMPROVEMENT_METRIC = "route-optimiser-service.cost-improvement";
    private static final String RUNTIME_METRIC = "route-optimiser-service.runtime";

    private MeterRegistry registry;
    private AtomicLong improvement;
    private AtomicLong runtime;
    private Gauge.Builder costImprovementGauge;
    private Gauge.Builder runtimeGauge;

    public OptimisedRouteReporter(MeterRegistry registry) {
        this.registry = registry;
        this.improvement = new AtomicLong(0);
        this.runtime = new AtomicLong(0);
        this.costImprovementGauge = Gauge.builder(COST_IMPROVEMENT_METRIC, improvement, (i) -> i.get());
        this.runtimeGauge = Gauge.builder(RUNTIME_METRIC, runtime, (r) -> r.get());
    }

    public void report(
        List<Route> unOptimisedRoutes, List<Route> optimisedRoutes,
        long optimisationTime, RouteOptimisationStrategy strategy
    ) {
        Map<String, Route> routesMap = unOptimisedRoutes
            .stream()
            .collect(Collectors.toMap(Route::getRouteId, Function.identity()));
        costImprovementGauge.tag("strategy", strategy.name()).register(registry);
        runtimeGauge.tag("strategy", strategy.name()).register(registry);

        double totalImprovement = 0.0;

        for (Route route : optimisedRoutes) {
            double costBefore = routesMap.get(route.getRouteId()).cost();
            double costAfter = route.cost();
            double costImprovement = costBefore - costAfter;

            totalImprovement += costImprovement;
        }

        double averageImprovement = totalImprovement / (double) optimisedRoutes.size();

        improvement.set(Double.valueOf(averageImprovement).longValue());
        runtime.set(optimisationTime);

        logger.info("[Reporter]: Average cost improvement of {} routes, {}", optimisedRoutes.size(), averageImprovement);
        logger.info("[Reporter]: Time taken to optimise {} routes, {} ms", optimisedRoutes.size(), optimisationTime);
    }
}
