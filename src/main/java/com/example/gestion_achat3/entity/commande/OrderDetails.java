package com.example.gestion_achat2.entity.commande;

import com.example.gestion_achat2.entity.achat.Product;
import com.example.gestion_achat2.entity.fournisseur.Proforma;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "proforma_id")
    private Proforma proforma;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}