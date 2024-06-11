package com.example.iotDataGenerator.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry(); // Or choose the appropriate MeterRegistry implementation
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
