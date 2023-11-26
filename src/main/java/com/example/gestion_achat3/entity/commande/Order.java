package com.example.gestion_achat3.entity.commande;

import com.example.gestion_achat3.entity.achat.Request;
import com.example.gestion_achat3.entity.fournisseur.Supplier;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_M")
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

    @ManyToOne
    @JoinColumn(name = "user_last_validator_id")
    private User user_last_validator;

    @Column(name = "state")
    private Integer state;

    public void refuser(ConnexionBase connexionBase)
    {
        this.setState((this.getState()+1)*-1);
        this.setUser_last_validator(connexionBase.get_connected());
        connexionBase.getOrderRepository().save(this);
    }

    public void accepter(ConnexionBase connexionBase)
    {
        this.setState(this.getState()+1);
        this.setUser_last_validator(connexionBase.get_connected());
        if(this.getState()==4)
        {
            //mandefa mail
        }
        connexionBase.getOrderRepository().save(this);
    }


    /*public List<Request> get_requests(ConnexionBase connexionBase)
    {
        List<OrderDetails> orderDetailsList=connexionBase.getOrderDetailsRepository().findByOrder(this);
        for (OrderDetails orderDetails:orderDetailsList)
        {

        }
    }*/

}