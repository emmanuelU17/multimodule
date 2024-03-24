package dev.fullstack;

import java.util.Optional;

interface ICar {

    Optional<Car> carById(long id);
    void save(Car car);

}
