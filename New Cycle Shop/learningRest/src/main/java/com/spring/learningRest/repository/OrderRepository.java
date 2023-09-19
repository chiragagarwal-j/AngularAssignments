package com.spring.learningRest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.learningRest.entity.Order;
import com.spring.learningRest.entity.User;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);
    List<Order> findAll();

}
