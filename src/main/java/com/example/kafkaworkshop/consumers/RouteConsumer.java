package com.example.kafkaworkshop.consumers;

import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.repository.RouteRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {
    private RouteRepository routeRepository;

    public RouteConsumer(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @KafkaListener(topics = "travel_routes", containerFactory = "routeKafkaListenerContainerFactory")
    public void consumerRoute(@Payload Route route) {
        routeRepository.addRoute(route);
    }
}
