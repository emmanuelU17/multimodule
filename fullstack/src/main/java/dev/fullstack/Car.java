package dev.fullstack;

import java.util.Date;

public record Car (Long carId, String brand, Date createAt) {
    Car(String brand, Date createAt) {
       this(null, brand, createAt);
    }
}