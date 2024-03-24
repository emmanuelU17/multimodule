package dev.fullstack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@Transactional
class ICarTest {

    @TestConfiguration
    static class ICarImpl {
        @Bean
        public ICar repository(JdbcClient client) {
            return new CarService(client);
        }
    }

    @Autowired
    private ICar repository;

    @Test
    void carById() {
        // given
        Date date = new Date();
        repository.save(new Car("Jeep Wrangler", date));
        repository.save(new Car("BMW", date));

        // when
        var optional = repository.carById(2);

        // then
        assertTrue(optional.isPresent());
        assertEquals(2L, optional.get().carId());
        assertEquals("BMW", optional.get().brand());
    }

    @Test
    void shouldSaveACar() {
        var tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);

        repository.save(new Car("Jeep Wrangler", new Date()));
        repository.save(new Car("Tesla", Date.from(tomorrow)));
    }

    @Test
    void shouldThrowConstraintError() {
        // given
        var tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        repository.save(new Car("Tesla", new Date()));

        // when
        assertThrows(DuplicateKeyException.class,
                () -> repository.save(new Car("Tesla", Date.from(tomorrow))));
    }

}