package com.example.gestion_achat2.entity.commande;

import com.example.gestion_achat2.entity.fournisseur.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "date_envoie")
    private LocalDate date_envoie;

    @Column(name = "prix_ttc")
    private Double prix_ttc;

    @Column(name = "tva")
    private Double tva;

    @Column(name = "prix_ht")
    private Double prix_ht;

}