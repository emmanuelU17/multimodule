package dev.fullstack;

import java.io.Serializable;
import java.util.Date;

class CarMapper implements Serializable {

    private long cart_id;
    private String brand;
    private Date created_at;

    public CarMapper() {}

    public CarMapper (long cart_Id, String brand, Date created_at) {
        this.cart_id = cart_Id;
        this.brand = brand;
        this.created_at = created_at;
    }

    public long cartId() {
        return cart_id;
    }

    public String brand() {
        return brand;
    }

    public Date createdAt() {
        return created_at;
    }

}
