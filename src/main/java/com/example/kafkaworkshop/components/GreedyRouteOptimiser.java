package com.example.kafkaworkshop.components;

import com.example.kafkaworkshop.domain.Location;
import com.example.kafkaworkshop.domain.Route;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * An optimiser that greedily picks the shortest path to the next city
 * to construct a full route that covers all locations.
 * Will produce routes of reasonable costs but not guaranteed to be optimal.
 */
@Component
public class GreedyRouteOptimiser implements RouteOptimiser {
    static class Node {
        Location location;
        double distance;

        public Node(Location location, double distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    private DistanceCalculator distanceCalculator;

    public GreedyRouteOptimiser(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public Route optimiseRoute(Route route) {
        Map<Location, List<Node>> graph = generateGraph(route);
        List<Location> optimisedRoute = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();
        Location currentLocation = route.randomRouteLocation();
        visited.add(currentLocation.getCity());

        while (currentLocation != null) {
            optimisedRoute.add(currentLocation);
            Location nextClosestLocation = null;

            for (Node node : graph.get(currentLocation)) {
                if (visited.contains(node.location.getCity())) {
                    continue;
                }

                nextClosestLocation = node.location;
                break;
            }

            if (nextClosestLocation != null) {
                visited.add(nextClosestLocation.getCity());
            }

            currentLocation = nextClosestLocation;
        }

        return new Route(route.getRouteId(), optimisedRoute);
    }

    private Map<Location, List<Node>> generateGraph(Route route) {
        Map<Location, List<Node>> graph = new HashMap<>();

        for (Location location : route.getRoute()) {
            graph.put(location, new ArrayList<>());
        }

        for (int i = 0; i < route.getRoute().size(); i++) {
            Location startLocation = route.getRoute().get(i);

            for (int j = 0; j < route.getRoute().size(); j++) {
                if (i == j) continue;
                Location endLocation = route.getRoute().get(j);
                double distance = distanceCalculator
                        .calculateDistance(startLocation.getLatitude(), startLocation.getLongitude(),
                            endLocation.getLatitude(), endLocation.getLongitude());
                Node edge = new Node(endLocation, distance);
                List<Node> edges = graph.get(startLocation);
                edges.add(edge);
                graph.put(startLocation, edges);
            }
        }

        for (List<Node> nodes : graph.values()) {
            Collections.sort(nodes, Comparator.comparing(n -> n.distance));
        }

        return graph;
    }
}
