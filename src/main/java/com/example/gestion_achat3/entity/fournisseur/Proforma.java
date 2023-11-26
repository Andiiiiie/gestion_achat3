package com.example.gestion_achat3.entity.fournisseur;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "prix_ht")
    private Double prix_ht;

    @Column(name = "prix_ttc")
    private Double prix_ttc;

    @Column(name = "tva")
    private Double tva;

    @Column(name = "date_demande")
    private LocalDate date_demande;


    public List<ProformaDetails> get_proforma(ConnexionBase connexionBase)
    {
        List<ProformaDetails> proformaDetailsList=connexionBase.getProformaDetailsRepository().findByProforma(this);

        return proformaDetailsList;
    }

    @Column(name = "state")
    private Integer state;

    public double get_Prix_produit(ConnexionBase connexionBase, Product product)
    {
        return connexionBase.getProformaDetailsRepository().findByProformaAndProduct(this,product).getUnitPrice();
    }


}