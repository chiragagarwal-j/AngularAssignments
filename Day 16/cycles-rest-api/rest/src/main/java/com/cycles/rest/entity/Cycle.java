package com.cycles.rest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cycles")
public class Cycle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String color;
    private String brand;
    private int quantity;
    private int price;
    private int numBorrowed = 0;
    private boolean addToCart;

    public int getNumAvailable() {
        return quantity - numBorrowed;
    }
}