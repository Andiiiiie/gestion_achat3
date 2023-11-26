package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.achat.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    @Query("select p from Purchase p where p.state < ?1")
    List<Purchase> findByStateLessThan(Integer state);

    @Query("select p from Purchase p where p.nbrProforma = ?1 and p.state = ?2")
    List<Purchase> findByNbrProformaAndState(Integer nbrProforma, Integer state);
}