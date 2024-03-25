package dev.fullstack;

import java.io.Serializable;
import java.util.Date;

public record Car (Long carId, String brand, Date createAt) implements Serializable {
    Car(String brand, Date createAt) {
       this(null, brand, createAt);
    }
}