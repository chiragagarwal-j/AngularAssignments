package com.cycles.rest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "cycle_id", referencedColumnName = "cycleId")
    private long cycleId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private int userID;

    @JoinColumn(name = "color", referencedColumnName = "color")
    private String color;

    @JoinColumn(name = "brand", referencedColumnName = "brand")
    private String brand;

    @JoinColumn(name = "quantity", referencedColumnName = "quantity")
    private int quantity;

    @JoinColumn(name = "price", referencedColumnName = "price")
    private int price;

    private boolean ordered;

}
