package dev.fullstack;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
class CarService implements ICar {

    private final JdbcClient client;

    public CarService(JdbcClient jdbcClient) {
        this.client = jdbcClient;
    }

    /**
     * Converts {@link Date} to UTC zone.
     *
     * @param date to convert to UTC zone.
     * @return {@link Date} in UTC timezone.
     */
    static Date toUTC(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar.getTime();
    }

    /**
     * Returns a {@link Car} by its primary key.
     *
     * @param id unique key for every {@link Car} object.
     * @return Optional containing the {@link Car} object or empty.
     * */
    @Transactional
    @Override
    public Optional<Car> carById(long id) {
        record CarMapper (long cart_Id, String brand, Date created_at) {}

        String sql = """
        SELECT c.cart_id, c.brand, c.created_at
        FROM car c WHERE c.cart_id = :id
        LIMIT 1;
        """;
        return client.sql(sql)
                .param("id", id)
                .query(CarMapper.class)
                .optional()
                .map(m -> new Car(m.cart_Id, m.brand, m.created_at));
    }

    /**
     * Saves a {@link Car} to the database.
     *
     * @param car the {@link Car} to save.
     * @throws IllegalStateException if the car object cannot be saved.
     * @throws java.sql.SQLException if unique constraint exception occurs.
     * */
    @Transactional
    @Override
    public void save(Car car) {
        var date = toUTC(car.createAt());
        var create = client.sql("INSERT INTO car(brand, created_at) values(?,?)")
                .params(List.of(car.brand(), date))
                .update();

        Assert.state(create == 1, "failed to create car " + car.brand());
    }

}
