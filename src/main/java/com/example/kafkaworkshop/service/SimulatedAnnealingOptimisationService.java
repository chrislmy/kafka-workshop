package com.example.kafkaworkshop.service;

import com.example.kafkaworkshop.components.RouteOptimiser;
import com.example.kafkaworkshop.components.SimulatedAnnealingOptimiser;
import com.example.kafkaworkshop.domain.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class SimulatedAnnealingOptimisationService implements RouteOptimisationService {

    private SimulatedAnnealingOptimiser optimiser;
    private ExecutorService executorService;
    @Value("${application.route-optimiser.simulated-annealing.sub-problem-size}")
    private int subProblemSize;

    public SimulatedAnnealingOptimisationService(SimulatedAnnealingOptimiser optimiser, ExecutorService executorService) {
        this.optimiser = optimiser;
        this.executorService = executorService;
    }

    @Override
    public List<Route> getOptimisedRoutes(List<Route> routes) throws Exception {
        List<Callable<List<Route>>> tasks = generateSubProblems(routes)
            .stream()
            .map((subProblem) -> new OptimisationTask(subProblem, optimiser))
            .collect(Collectors.toList());
        List<Route> optimisedRoutes = new ArrayList<>();

        List<Future<List<Route>>> futures = executorService.invokeAll(tasks);

        for (Future<List<Route>> future : futures) {
            List<Route> optimisedSubProblem = future.get();
            optimisedSubProblem
                .stream()
                .forEach((route) -> {
                    optimisedRoutes.add(route);
                });
        }

        return optimisedRoutes;
    }

    /**
     * Break the main problem into sub problems that will be solved in parallel
     */
    private List<List<Route>> generateSubProblems(List<Route> routes) {
        int numberOfArrays = routes.size() / subProblemSize;
        int remainder = routes.size() % subProblemSize;
        List<List<Route>> subProblems = new ArrayList<>();

        int offset = 0;

        while (numberOfArrays > 0) {
            List<Route> subProblem = new ArrayList<>();

            for (int i = 0; i < subProblemSize; i++) {
                subProblem.add(routes.get(i + offset));
            }

            offset += subProblemSize;
            numberOfArrays--;
            subProblems.add(subProblem);
        }

        if (remainder > 0) {
            List<Route> subProblem = new ArrayList<>();
            for (int i = 0; i < remainder; i++) {
                subProblem.add(routes.get(i + offset));
            }
            subProblems.add(subProblem);
        }

        return subProblems;
    }

    static class OptimisationTask implements Callable<List<Route>> {
        private List<Route> routes;
        private RouteOptimiser optimiser;

        public OptimisationTask(List<Route> routes, RouteOptimiser optimiser) {
            this.routes = routes;
            this.optimiser = optimiser;
        }

        @Override
        public List<Route> call() throws Exception {
            return routes
                .stream()
                .map(optimiser::optimiseRoute)
                .collect(Collectors.toList());
        }
    }
}
