package com.example.gestion_achat2.entity.achat;

import com.example.gestion_achat2.entity.global.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "user_validator_id")
    private User userValidator;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}