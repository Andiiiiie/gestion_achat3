package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.achat.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}