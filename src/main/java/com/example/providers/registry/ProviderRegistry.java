package com.example.providers.registry;

import com.example.providers.model.Provider;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProviderRegistry {

    private final RestClient.Builder clientBuilder;
    private volatile Map<String, RestClient> clients = Map.of();

    public ProviderRegistry(RestClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public void replaceAll(List<Provider> providers) {
        this.clients = providers.stream()
                .collect(Collectors.toUnmodifiableMap(Provider::id, this::createClient));
    }

    public RestClient getClient(String providerId) {
        RestClient client = clients.get(providerId);
        if (client == null) {
            throw new IllegalArgumentException("Unknown provider: " + providerId);
        }
        return client;
    }

    private RestClient createClient(Provider provider) {
        return clientBuilder.clone()
                .baseUrl(provider.baseUrl())
                .defaultHeader("X-Api-Key", provider.apiKey())
                .build();
    }
}
