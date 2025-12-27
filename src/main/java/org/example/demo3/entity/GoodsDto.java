package org.example.demo3.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GoodsDto {
    private int id;
    private String name;
    private double price;
    @Enumerated(EnumType.STRING)
    private Type type;
    private int quantity;

    public GoodsDto(int id, String name, Type type ,  double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
