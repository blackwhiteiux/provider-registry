package com.example.providers.source;

import com.example.providers.model.Provider;

import java.util.List;

public interface ProviderSource {
    List<Provider> load();
}
