package com.example.kafkaworkshop.config;

import com.example.kafkaworkshop.domain.Route;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.bootstrap-servers}")
    private String brokers;

    private ObjectMapper objectMapper;

    public KafkaConsumerConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public JsonDeserializer<Route> consumerDeserializer() {
        return new JsonDeserializer<>(objectMapper);
    }

    @Bean
    public ConsumerFactory<String, Route> routeConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Route.class);
        DefaultKafkaConsumerFactory<String, Route> consumerFactory =
                new DefaultKafkaConsumerFactory<>(props);
        consumerFactory.setValueDeserializer(consumerDeserializer());
        return consumerFactory;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Route>>
    routeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Route> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(routeConsumerFactory());
        factory.setConcurrency(3);
        return factory;
    }
}
