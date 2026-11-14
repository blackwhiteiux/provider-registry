package com.example.providers.config;

import com.example.providers.registry.ProviderRegistry;
import com.example.providers.service.PhoneAvailabilityService;
import com.example.providers.source.JsonFileProviderSource;
import com.example.providers.source.ProviderSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;

@Configuration
public class ProvidersConfig {

    @Bean
    ProviderSource providerSource(@Value("${providers.file:providers.json}") String file) {
        return new JsonFileProviderSource(Path.of(file));
    }

    @Bean
    ProviderRegistry providerRegistry(ProviderSource source) {
        ProviderRegistry registry = new ProviderRegistry(RestClient.builder());
        registry.replaceAll(source.load());
        return registry;
    }

    @Bean
    PhoneAvailabilityService phoneAvailabilityService(ProviderRegistry registry) {
        return new PhoneAvailabilityService(registry);
    }
}
