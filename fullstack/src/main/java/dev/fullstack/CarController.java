package dev.fullstack;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "${api.base.path}/car")
class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Car carById(@PathVariable(name = "id") Long id) {
        return service.carById(id).orElse(null);
    }

    record CartReq(String brand) {}

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public void create(@Valid @RequestBody CartReq req) {
        service.save(new Car(req.brand(), new Date()));
    }

}
