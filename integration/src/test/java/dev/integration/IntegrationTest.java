package dev.integration;

import dev.webserver.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrationTest {

    private final String path = "/api/v1";

    private WebTestClient testClient;

    // https://java.testcontainers.org/features/creating_images/
    @Container
    static final GenericContainer<?> container = new GenericContainer<>(
            new ImageFromDockerfile("native-image", false)
            .withDockerfile(Paths.get("../Dockerfile")))
            .withExposedPorts(8080);

    @BeforeEach
    void setUp() {
        container.start();
        String endpoint = String
                .format("http://%s:%d/", container.getHost(), container.getFirstMappedPort());

        testClient = WebTestClient.bindToServer().baseUrl(endpoint).build();
    }

    @Test
    public void healthy() {
        testClient.get().uri(path + "/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    @Test
    public void payload() {
        testClient.get().uri(path)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Hello World!");
    }

    @Test
    void sampleTestAgainstWebServerObject() {
        assertEquals(new Sample("frank"), new Sample("frank"));
    }

}