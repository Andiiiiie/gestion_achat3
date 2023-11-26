package com.example.gestion_achat3.entity.fournisseur;

import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.commande.Order;
import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.model.PurchaseMetier;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    public void demande_proformat(ConnexionBase connexionBase,List<Purchase> purchaseList)
    {

        Proforma proforma=new Proforma();
        proforma.setSupplier(this);
        proforma.setDate_demande(LocalDate.now());
        proforma.setState(0);
        connexionBase.getProformaRepository().save(proforma);
        for(Purchase purchase:purchaseList) {
            ProformaDetails proformaDetails = new ProformaDetails();
            proformaDetails.setProforma(proforma);
            proformaDetails.setProduct(purchase.getProduct());
            proformaDetails.setPurchase(purchase);
            connexionBase.getProformaDetailsRepository().save(proformaDetails);

            purchase.setState(purchase.getState()+1);
            connexionBase.getPurchaseRepository().save(purchase);
        }

    }

    public List<Purchase> get_achats_sans_demande(ConnexionBase connexionBase)
    {
        List<Purchase> purchaseList=new ArrayList<>();
        List<Purchase> purchases=connexionBase.getPurchaseRepository().findByStateLessThan(3);
        for (Purchase purchase:purchases)
        {
            if(purchase.demande_envoyee(connexionBase,this).equals(false))
            {
                purchaseList.add(purchase);
            }
        }
        return purchaseList;
    }


    public List<OrderDetails> get_orders(ConnexionBase connexionBase)
    {
        List<OrderDetails> orderDetailsList=connexionBase.getOrderDetailsRepository().findByProforma_Supplier_order_null(this);
        return orderDetailsList;
    }


    public Order get_bon_de_commande(ConnexionBase connexionBase)
    {
        List<OrderDetails> orderDetailsList=connexionBase.getOrderDetailsRepository().findByProforma_Supplier_order_null(this);
        if(orderDetailsList.size()==0)
        {
            throw  new RuntimeException("aucune commande ");
        }
        Order order=new Order();
        order.setSupplier(this);
        connexionBase.getOrderRepository().save(order);
        double totalTVA=0;
        double totalHT=0;
        double totalTTC=0;
        for (OrderDetails orderDetails:orderDetailsList)
        {
            orderDetails.setOrder(order);
            connexionBase.getOrderDetailsRepository().save(orderDetails);
            totalTVA+=orderDetails.getProduct().get_tva(orderDetails.getProforma().get_Prix_produit(connexionBase,orderDetails.getProduct()))*orderDetails.getQuantity();
            totalHT+=(orderDetails.getProforma().get_Prix_produit(connexionBase,orderDetails.getProduct())-orderDetails.getProduct().get_tva(orderDetails.getProforma().get_Prix_produit(connexionBase,orderDetails.getProduct())))*orderDetails.getQuantity();
            totalTTC=orderDetails.getProforma().get_Prix_produit(connexionBase,orderDetails.getProduct())*orderDetails.getQuantity();
        }
        order.setTva(totalTVA);
        order.setPrix_ht(totalHT);
        order.setPrix_ttc(totalTTC);
        order.setState(0);
        connexionBase.getOrderRepository().save(order);
        return order;
    }




}