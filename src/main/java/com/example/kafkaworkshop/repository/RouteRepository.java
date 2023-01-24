package com.example.kafkaworkshop.repository;

import com.example.kafkaworkshop.domain.Route;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

// A placeholder in memory data store to store routes consumed temporarily.
// In a production environment, this would be a real db.
@Component
public class RouteRepository {
    private Queue<Route> routes;

    public RouteRepository(Queue<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public List<Route> getBatchOfRoutes(int batchSize) {
        List<Route> result = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            if (!routes.isEmpty()) {
                result.add(routes.remove());
            }
        }
        return result;
    }
}
