package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.achat.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.state = ?1")
    List<Request> findByState(Integer state);

    @Query("select r from Request r where r.product = ?1 and r.state = ?2 and r.purchase is null ")
    List<Request> findByProductAndState(Product product, Integer state);
}