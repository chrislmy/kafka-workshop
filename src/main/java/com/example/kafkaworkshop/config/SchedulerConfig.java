package com.example.kafkaworkshop.config;

import com.example.kafkaworkshop.domain.ApplicationParameters;
import com.example.kafkaworkshop.domain.WithParameters;
import com.example.kafkaworkshop.domain.WorkerParameters;
import com.example.kafkaworkshop.repository.ParametersRepository;
import com.example.kafkaworkshop.scheduledjobs.RouteOptimiserScheduledJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer, WithParameters<WorkerParameters> {

    @Value("${application.workers.route-optimiser.processing-delay}")
    private long processingDelay;

    private ParametersRepository parametersRepository;
    private RouteOptimiserScheduledJob routeOptimiserScheduledJob;

    public SchedulerConfig(ParametersRepository parametersRepository, RouteOptimiserScheduledJob routeOptimiserScheduledJob) {
        this.parametersRepository = parametersRepository;
        this.routeOptimiserScheduledJob = routeOptimiserScheduledJob;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
            () -> routeOptimiserScheduledJob.optimiseRoutes(),
            triggerContext -> {
                Optional<Date> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletionTime());
                Optional<ApplicationParameters> params = parametersRepository.getLatest();
                params.ifPresent((parameters -> setParameters(parameters.getWorkerParameters())));
                Instant nextExecutionTime = lastCompletionTime.orElseGet(Date::new).toInstant().plusMillis(processingDelay);
                return Date.from(nextExecutionTime).toInstant();
            });
    }

    @Override
    public void setParameters(WorkerParameters params) {
        this.processingDelay = params.getProcessingDelay();
    }
}
