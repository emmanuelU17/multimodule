package dev.integration;

import dev.fullstack.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FullStackModuleTest {

    private static final Logger log = LoggerFactory.getLogger(FullStackModuleTest.class);
    static final Network network = Network.newNetwork();
    private static WebTestClient testClient;

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("fullstack_db")
            .withUsername("fullstack")
            .withPassword("fullstack")
            .withNetwork(network)
            .withNetworkAliases("mysql");

    @Container
    static final GenericContainer<?> server = new GenericContainer<>(
            new ImageFromDockerfile("fullstack-module", false)
                    .withDockerfile(Paths.get("../Dockerfile")))
            .withExposedPorts(8081)
            .withNetwork(network)
            .dependsOn(mysql)
            .withLogConsumer(new Slf4jLogConsumer(log));

    @BeforeAll
    static void beforeAllTests() {
        mysql.start();

        assertTrue(mysql.isCreated());
        assertTrue(mysql.isRunning());

        server
                .withEnv(Map.of(
                        "DB_CONNECTION_STR", "jdbc:mysql://mysql:3306/fullstack_db",
                        "PORT", String.valueOf(8081),
                        "DB_USERNAME", mysql.getUsername(),
                        "DB_PASSWORD", mysql.getPassword()
                ))
                .waitingFor(Wait.forHttp("/actuator/health"))
                .start();

        assertTrue(server.isCreated());
        assertTrue(server.isRunning());

        String endpoint = String
                .format("http://%s:%d/", server.getHost(), server.getFirstMappedPort());

        testClient = WebTestClient.bindToServer().baseUrl(endpoint).build();
    }

    @Test
    void canImportFromFullStackModule() {
        Date date = new Date();
        assertEquals(new Car(1L, "BMW", date),
                new Car(1L, "BMW", date));
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

        // create route
        testClient.post().uri("/api/v1/car")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Payload("BMW")))
                .exchange()
                .expectStatus()
                .isCreated();

        // get route
        testClient.get().uri("/api/v1/car/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.brand")
                .isEqualTo("BMW")
                .jsonPath("$.carId")
                .isEqualTo(1);
    }

}
