package com.example.gestion_achat2.entity.fournisseur;

import com.example.gestion_achat2.entity.achat.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "proforma")
public class Proforma {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "date_reception")
    private LocalDate date_reception;

    @Column(name = "ref")
    private String ref;

    @Column(name = "prix_unitaire")
    private Double prix_unitaire;

    @Column(name = "quantite_dispo")
    private Integer quantite_dispo;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}