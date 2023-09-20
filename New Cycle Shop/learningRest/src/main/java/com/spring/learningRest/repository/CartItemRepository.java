package com.spring.learningRest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.learningRest.entity.CartItem;
import com.spring.learningRest.entity.Cycle;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
    
    @Query(value="SELECT ci FROM CartItem ci WHERE ci.cycle = :cycle")
    Optional<CartItem> findByProduct(Cycle cycle);
}
