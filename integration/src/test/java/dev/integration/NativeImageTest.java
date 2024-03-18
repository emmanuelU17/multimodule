package dev.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.nio.file.Paths;

public class NativeImageTest {

    private static WebTestClient testClient;

    // https://java.testcontainers.org/features/creating_images/
    @Container
    static final GenericContainer<?> container = new GenericContainer<>(
            new ImageFromDockerfile("native-image", false)
                    .withDockerfile(Paths.get("../Dockerfile.native")))
            .withImagePullPolicy(PullPolicy.defaultPolicy())
            .withExposedPorts(8080);

//    @Container
//    static final GenericContainer<?> container = new GenericContainer<>("native-image:latest")
//            .withImagePullPolicy(PullPolicy.defaultPolicy())
//            .withExposedPorts(8081);

    @BeforeAll
    static void setUp() {
        container.start();
        String endpoint = String
                .format("http://%s:%d/", container.getHost(), container.getFirstMappedPort());

        testClient = WebTestClient.bindToServer().baseUrl(endpoint).build();
    }

    @Test
    public void actuatorHealthyNativeImage() {
        testClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("UP");
    }

    @Test
    public void payloadNativeImage() {
        testClient.get().uri("/api/v1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo("Hello world!");
    }

}
