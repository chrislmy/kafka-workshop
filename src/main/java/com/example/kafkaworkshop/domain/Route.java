package com.example.kafkaworkshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
}
