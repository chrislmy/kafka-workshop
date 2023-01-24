package com.example.kafkaworkshop.domain;

import com.example.kafkaworkshop.components.DistanceCalculator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Random;

public class Route {

    @JsonProperty("routeId")
    private String routeId;

    @JsonProperty("route")
    private List<Location> route;

    public Route(String routeId, List<Location> route) {
        this.routeId = routeId;
        this.route = route;
    }

    public Route() {
    }

    public String getRouteId() {
        return routeId;
    }

    public List<Location> getRoute() {
        return route;
    }

    public Location randomRouteLocation() {
        Random rand = new Random();
        return route.get(rand.nextInt(route.size()));
    }

    public double cost(DistanceCalculator distanceCalculator) {
        double distance = 0.0;

        if (route.isEmpty()) {
            return distance;
        }

        Location prev = route.get(0);

        for (int i = 1; i < route.size(); i++) {
            Location current = route.get(i);
            double distanceToPrev = distanceCalculator.calculateDistance(current.getLatitude(), current.getLongitude(),
                    prev.getLatitude(), prev.getLongitude());
            distance += distanceToPrev;
        }

        return distance;
    }
}
