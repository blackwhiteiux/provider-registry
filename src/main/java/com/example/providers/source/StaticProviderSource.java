package com.example.providers.source;

import com.example.providers.model.Provider;

import java.util.List;

public class StaticProviderSource implements ProviderSource {

    private final List<Provider> providers;

    public StaticProviderSource(List<Provider> providers) {
        this.providers = List.copyOf(providers);
    }

    @Override
    public List<Provider> load() {
        return providers;
    }
}
