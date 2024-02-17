package com.sapient.booking.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("booking-service-api")
                .packagesToScan("com.sapient.booking") // Specify the package to scan
                .build();
    }
}
