package com.example.kafkaworkshop.domain;

import com.example.kafkaworkshop.components.HaversineDistanceCalculator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public double cost() {
        double distance = 0.0;

        if (route.isEmpty()) {
            return distance;
        }

        for (int i = 0; i < route.size() - 1; i++) {
            Location prev = route.get(i);
            Location current = route.get(i + 1);
            double distanceToPrev = HaversineDistanceCalculator.calculateDistance(current.getLatitude(), current.getLongitude(),
                    prev.getLatitude(), prev.getLongitude());
            distance += distanceToPrev;
        }

        return distance;
    }

    public Route clone() {
        List<Location> locationsClone = route
                .stream()
                .map(Location::clone)
                .collect(Collectors.toList());
        return new Route(routeId, locationsClone);
    }

    public void randomiseRoute(Random random) {
        int index1 = random.nextInt(route.size());
        int index2 = random.nextInt(route.size());
        Collections.swap(route, index1, index2);
    }
}
