package com.example.kafkaworkshop.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

    private RouteOptimiserWorker routeOptimiserWorker;

    public ApplicationStartupListener(RouteOptimiserWorker routeOptimiserWorker) {
        this.routeOptimiserWorker = routeOptimiserWorker;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            routeOptimiserWorker.start();
        } catch (InterruptedException e) {
            logger.error("An error occurred on the route optimiser worker {}", e.getMessage());
        }
    }
}
