package dev.integration;

import dev.webserver.Sample;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {

    private static WebTestClient testClient;

    @Container
    private static final GenericContainer<?> container = new GenericContainer<>(
            new ImageFromDockerfile("normal-image", false)
            .withDockerfile(Paths.get("../Dockerfile")))
            .withExposedPorts(8080);

    @BeforeAll
    static void setUp() {
        container.start();
        String endpoint = String
                .format("http://%s:%d/", container.getHost(), container.getFirstMappedPort());

        testClient = WebTestClient.bindToServer().baseUrl(endpoint).build();
    }

    @Test
    void healthy() {
        testClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("UP");
    }

    @Test
    void payload() {
        testClient.get().uri("/api/v1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo("Hello world!");
    }

    @Test
    void sampleTestAgainstWebServerObject() {
        assertEquals(new Sample("frank"), new Sample("frank"));
    }

}