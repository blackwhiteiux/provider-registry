package com.example.providers.service;

import com.example.providers.model.Provider;
import com.example.providers.registry.ProviderRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PhoneAvailabilityServiceTest {

    private static final String PHONE = "79991234567";

    private MockRestServiceServer server;
    private PhoneAvailabilityService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();

        ProviderRegistry registry = new ProviderRegistry(builder);
        registry.replaceAll(List.of(new Provider("a", "https://a.example", "key-a")));
        service = new PhoneAvailabilityService(registry);
    }

    @Test
    void status200Ok_numberIsAvailable() {
        server.expect(requestTo("https://a.example/subscribers/" + PHONE))
                .andExpect(method(GET))
                .andExpect(header("X-Api-Key", "key-a"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        assertTrue(service.isAvailable("a", PHONE));
        server.verify();
    }

    @Test
    void status404_numberIsNotAvailable() {
        server.expect(requestTo("https://a.example/subscribers/" + PHONE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertFalse(service.isAvailable("a", PHONE));
        server.verify();
    }
}
