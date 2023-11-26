package com.example.gestion_achat3.entity.achat;

import com.example.gestion_achat3.model.PurchaseMetier;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Column(name = "designation")
    private String designation;

    @Column(name = "tva")
    private Double tva;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    public PurchaseMetier get_achat(ConnexionBase connexionBase)
    {
        PurchaseMetier purchaseMetier=new PurchaseMetier();
        List<Request> requestList=connexionBase.getRequestRepository().findByProductAndState(this,10);
        int quantite=0;
        for (Request request:requestList)
        {
            quantite+=request.getQuantity();
            System.out.println("tato "+quantite);
        }
        Purchase purchase=new Purchase();
        purchase.setState(0);
        purchase.setProduct(this);
        purchase.setQuantity(quantite);
        purchaseMetier.setPurchase(purchase);
        purchaseMetier.setRequestList(requestList);
        return purchaseMetier;
    }

    public double get_tva(double prix_unitaire)
    {
        return (getTva()*prix_unitaire)/100;
    }



}