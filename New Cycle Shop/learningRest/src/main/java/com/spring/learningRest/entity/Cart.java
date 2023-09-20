package com.spring.learningRest.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();
}
