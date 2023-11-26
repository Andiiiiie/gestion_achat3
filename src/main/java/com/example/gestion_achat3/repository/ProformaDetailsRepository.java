package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.fournisseur.Proforma;
import com.example.gestion_achat3.entity.fournisseur.ProformaDetails;
import com.example.gestion_achat3.entity.fournisseur.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProformaDetailsRepository extends JpaRepository<ProformaDetails, Long> {
    @Query("select p from ProformaDetails p where p.proforma.supplier = ?1 and p.purchase = ?2")
    List<ProformaDetails> findByProforma_SupplierAndPurchase(Supplier supplier, Purchase purchase);

    @Query("select p from ProformaDetails p where p.proforma = ?1")
    List<ProformaDetails> findByProforma(Proforma proforma);

    @Query("select p from ProformaDetails p where p.purchase = ?1")
    List<ProformaDetails> findByPurchase(Purchase purchase);

    @Query("select p from ProformaDetails p where p.proforma = ?1 and p.product = ?2")
    ProformaDetails findByProformaAndProduct(Proforma proforma, Product product);
}