package dev.fullstack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestConfig {

    static final Logger log = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer() {
        try (var sql = new MySQLContainer<>("mysql:8.0")) {
            return sql.withDatabaseName("fullstack_db")
                    .withUsername("fullstack")
                    .withPassword("fullstack");
        } catch (RuntimeException ex) {
            log.error("failed to start up MySQL in test/dev mode");
            throw new RuntimeException();
        }
    }

}
