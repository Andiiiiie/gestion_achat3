package com.example.gestion_achat3.entity.commande;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.entity.fournisseur.Proforma;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Column(name = "unit_price")
    private Double unitPrice;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "proforma_id")
    private Proforma proforma;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}