package com.spring.learningRest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.learningRest.entity.CartItem;
import com.spring.learningRest.entity.Cycle;
import com.spring.learningRest.entity.Order;
import com.spring.learningRest.entity.User;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);
    List<Order> findAll();

    @Query(value="SELECT o FROM Order o WHERE o.user = :user AND o.cycle = :cycle")
    Optional<Order> findByUserAndProductOptional(User user, Cycle cycle);
}
