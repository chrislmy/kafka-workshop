package com.example.kafkaworkshop.config;

import com.example.kafkaworkshop.repository.RouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public RouteRepository routeRepository() {
        return new RouteRepository(new ConcurrentLinkedQueue<>());
    }
}
