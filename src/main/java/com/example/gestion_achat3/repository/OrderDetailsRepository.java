package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.commande.Order;
import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.entity.fournisseur.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    @Query("select o from OrderDetails o where o.proforma.supplier = ?1 and o.order is null ")
    List<OrderDetails> findByProforma_Supplier_order_null(Supplier supplier);

    @Query("select o from OrderDetails o where o.order = ?1")
    List<OrderDetails> findByOrder(Order order);
}