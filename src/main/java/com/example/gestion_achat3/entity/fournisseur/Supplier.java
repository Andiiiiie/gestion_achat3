package com.example.gestion_achat2.entity.fournisseur;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "supplier")
public class Supplier {
    @Column(name = "name")
    private String name;

    @Column(name = "contact")
    private String contact;

    @Column(name = "responsable")
    private String responsable;

    @Column(name = "adresse")
    private String adresse;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

}