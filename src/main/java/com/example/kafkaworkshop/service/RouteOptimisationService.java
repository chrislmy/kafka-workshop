package com.example.kafkaworkshop.service;

import com.example.kafkaworkshop.domain.Route;

import java.util.List;

public interface RouteOptimisationService {

    List<Route> getOptimisedRoutes(List<Route> routes);
}
