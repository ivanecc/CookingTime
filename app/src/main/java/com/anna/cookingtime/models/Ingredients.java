package com.anna.cookingtime.models;

import java.io.Serializable;

/**
 * Created by iva on 21.02.17.
 */
public class Ingredients implements Serializable{
    private long id;
    private String name;
    private float quantity;
    private String unit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
