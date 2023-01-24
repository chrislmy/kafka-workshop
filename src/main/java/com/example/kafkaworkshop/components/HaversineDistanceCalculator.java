package com.example.kafkaworkshop.components;

import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceCalculator implements DistanceCalculator {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    @Override
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat  = Math.toRadians((lat2 - lat1));
        double dLong = Math.toRadians((lng2 - lng1));

        double startLat = Math.toRadians(lat1);
        double endLat   = Math.toRadians(lat2);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c * 1000; // in metres
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
