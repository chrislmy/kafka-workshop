package com.example.kafkaworkshop.consumers;

import com.example.kafkaworkshop.domain.Route;
import com.example.kafkaworkshop.repository.RouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RouteConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RouteConsumer.class);

    private ObjectMapper objectMapper;

    private RouteRepository routeRepository;

    public RouteConsumer(ObjectMapper objectMapper, RouteRepository routeRepository) {
        this.objectMapper = objectMapper;
        this.routeRepository = routeRepository;
    }

    @KafkaListener(topics = "travel_routes", containerFactory = "routeKafkaListenerContainerFactory")
    public void consumerRoute(
        @Payload Route route,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) throws JsonProcessingException {
        routeRepository.addRoute(route);
    }
}
