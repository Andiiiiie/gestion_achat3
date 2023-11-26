package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.fournisseur.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}