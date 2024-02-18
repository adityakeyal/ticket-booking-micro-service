package com.sapient.booking.config;

import com.sapient.booking.service.external.ShowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ExternalServiceConfig {

    @Value("${ticket.service.theatre-service.url}")
    private String showServiceUrl;

    @Bean
    public ShowService showServiceProxy(){

        WebClient webClient = WebClient.builder()
                .baseUrl(showServiceUrl)
                .build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build();
        return httpServiceProxyFactory.createClient(ShowService.class);
    }



}
