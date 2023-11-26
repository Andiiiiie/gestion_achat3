package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.commande.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.state = ?1")
    List<Order> findByState(Integer state);
}