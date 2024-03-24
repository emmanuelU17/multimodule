package dev.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FullStackTest {

    private static WebTestClient testClient;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("fullstack_db")
            .withUsername("fullstack")
            .withPassword("fullstack");

    @Container
    static final GenericContainer<?> server = new GenericContainer<>(
            new ImageFromDockerfile("fullstack-module", false)
                    .withDockerfile(Paths.get("../Dockerfile.fullstack")))
            .withExposedPorts(8081);

    @BeforeAll
    static void beforeAllTests() {
        mysql.start();

        assertTrue(mysql.isCreated());
        assertTrue(mysql.isRunning());

        server
                .withEnv("SPRING_DATASOURCE_URL", mysql.getJdbcUrl())
                .withEnv("PORT", String.valueOf(8081))
                .withEnv("SPRING_DATASOURCE_USERNAME", mysql.getUsername())
                .withEnv("SPRING_DATASOURCE_PASSWORD", mysql.getPassword())
                .start();

        String logs = """
                MySQL credentials \n
                URL: %s \n
                Username: %s \n
                Password: %s \n
                
                Server logs \n
                %s
                """.formatted(mysql.getJdbcUrl(), mysql.getUsername(),
                mysql.getPassword(), server.getLogs());
        System.out.println(logs);

        String endpoint = String
                .format("http://%s:%d/", server.getHost(), server.getFirstMappedPort());

        testClient = WebTestClient.bindToServer().baseUrl(endpoint).build();
    }

    @Test
    void actuator() {
        testClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("UP");
    }

    @Test
    void shouldSuccessfullySaveACar() {
        record Payload(String brand) {
        }

        // create
        testClient.post().uri("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Payload("BMW")))
                .exchange()
                .expectStatus()
                .isCreated();

        // get
        testClient.get().uri("/api/v1/car/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.brand")
                .isEqualTo("UP")
                .jsonPath("$.carId")
                .isEqualTo(1);
    }

}