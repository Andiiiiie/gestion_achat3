package com.example.gestion_achat3.entity.achat;

import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "request")
public class Request {
    @ManyToOne
    @JoinColumn(name = "user_requester_id")
    private User user_requester;

    @ManyToOne
    @JoinColumn(name = "request_type_id")
    private Request_type request_type;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "reason")
    private String reason;

    //0 si en attente, 1 valide par superieur de service ,10 si valide par service achat
    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @Column(name = "requestdate")
    private LocalDate requestdate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_validator_2_id")
    private User user_validator2;

    @ManyToOne
    @JoinColumn(name = "user_validator_1_id")
    private User user_validator1;



    public void demander(ConnexionBase connexionBase)
    {
        if(reason.isEmpty())
        {
            throw  new RuntimeException("raison inexistante");
        }
        if(quantity<0)
        {
            throw  new RuntimeException("quantite invalide");
        }
        this.setQuantity(this.getQuantity());
        this.setUser_requester(connexionBase.get_connected());
        this.setState(0);
        this.setRequestdate(LocalDate.now());
        connexionBase.getRequestRepository().save(this);
    }


    public int valider(ConnexionBase connexionBase)
    {
        if(this.getState()==0)
        {
            this.setState(1);
            this.setUser_validator1(connexionBase.get_connected());
            connexionBase.getRequestRepository().save(this);
            return 1;
        }
        else
        {
            this.setState(10);
            this.setUser_validator2(connexionBase.get_connected());
            connexionBase.getRequestRepository().save(this);
            return 10;
        }
    }

    public int refuser(ConnexionBase connexionBase)
    {
        if(this.getState()==0)
        {
            this.setState(-1);
            this.setUser_validator1(connexionBase.get_connected());
            connexionBase.getRequestRepository().save(this);
            return 1;
        }
        else
        {
            this.setState(-10);
            this.setUser_validator2(connexionBase.get_connected());
            connexionBase.getRequestRepository().save(this);
            return 10;
        }
    }


    //getter and setter


    public void setUser_requester(User user_requester) {
        this.user_requester = user_requester;
    }

    public void setRequest_type(Request_type request_type) {
        this.request_type = request_type;
    }

    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void setRequestdate(LocalDate requestdate) {
        this.requestdate = requestdate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUser_validator2(User user_validator2) {
        this.user_validator2 = user_validator2;
    }

    public void setUser_validator1(User user_validator1) {
        this.user_validator1 = user_validator1;
    }
}