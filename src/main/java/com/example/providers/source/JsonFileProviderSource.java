package com.example.providers.source;

import com.example.providers.model.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

public class JsonFileProviderSource implements ProviderSource {

    private final Path file;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonFileProviderSource(Path file) {
        this.file = file;
    }

    @Override
    public List<Provider> load() {
        try {
            return List.of(mapper.readValue(file.toFile(), Provider[].class));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read providers file: " + file, e);
        }
    }
}
