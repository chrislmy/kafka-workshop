package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.domain.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OptimisedRouteReporter {

    private static final Logger logger = LoggerFactory.getLogger(OptimisedRouteReporter.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void report(List<Route> unOptimisedRoutes, List<Route> optimisedRoutes, long optimisationTime) {
        Map<String, Route> routesMap = unOptimisedRoutes
            .stream()
            .collect(Collectors.toMap(Route::getRouteId, Function.identity()));

        for (Route route : optimisedRoutes) {
            double costBefore = routesMap.get(route.getRouteId()).cost();
            double costAfter = route.cost();
            double costImprovement = costBefore - costAfter;

            logger.info("[Worker]: Cost: {}, before: {}, after: {}, improvement: {}",
                    route.getRouteId(), df.format(costBefore), df.format(costAfter), df.format(costImprovement));
        }

        logger.info("[Worker]: Time taken to optimise {} routes, {} ms", optimisedRoutes.size(), optimisationTime);
    }
}
