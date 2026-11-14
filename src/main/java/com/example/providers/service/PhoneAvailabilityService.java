package com.example.providers.service;

import com.example.providers.registry.ProviderRegistry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.client.HttpClientErrorException;


public class PhoneAvailabilityService {

    private final ProviderRegistry registry;

    public PhoneAvailabilityService(ProviderRegistry registry) {
        this.registry = registry;
    }

    public boolean isAvailable(String providerId, String phone) {
        try {
            SubscriberResponse body = registry.getClient(providerId).get()
                    .uri("/subscribers/{phone}", phone)
                    .retrieve()
                    .body(SubscriberResponse.class);
            return body != null && "ok".equals(body.status());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record SubscriberResponse(String status) {
    }
}
