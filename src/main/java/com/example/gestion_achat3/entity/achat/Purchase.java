package com.example.gestion_achat3.entity.achat;

import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.entity.fournisseur.ProformaDetails;
import com.example.gestion_achat3.entity.fournisseur.Supplier;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.model.PurchaseMetier;
import com.example.gestion_achat3.service.ConnexionBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

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


    @Column(name = "nbr_proforma")
    private Integer nbrProforma=0;

    public static List<PurchaseMetier> get_list(ConnexionBase connexionBase)
    {
        List<Product> productList=connexionBase.getProductRepository().findAll();
        List<PurchaseMetier> purchaseMetierList=new ArrayList<>();
        for(Product product1:productList)
        {
            PurchaseMetier purchaseMetier=product1.get_achat(connexionBase);
            if(purchaseMetier.getPurchase().getQuantity()!=0)
            {
                purchaseMetierList.add(purchaseMetier);
            }
        }
        return purchaseMetierList;
    }

    public Boolean demande_envoyee(ConnexionBase connexionBase, Supplier supplier)
    {
        List<ProformaDetails> proformaDetailsList=connexionBase.getProformaDetailsRepository().findByProforma_SupplierAndPurchase(supplier,this);
        if(proformaDetailsList.size()>0)
        {
            return true;
        }
        return false;
    }

    public static List<Purchase> get_purchase_with_3_proforma(ConnexionBase connexionBase)
    {
        List<Purchase> purchaseList=connexionBase.getPurchaseRepository().findByNbrProformaAndState(3,3);
        return purchaseList;
    }

    public List<ProformaDetails> get_proforma(ConnexionBase connexionBase)
    {
        List<ProformaDetails> proformaDetailsList=connexionBase.getProformaDetailsRepository().findByPurchase(this);

        return proformaDetailsList;
    }

    public int get_min(List<ProformaDetails> proformaDetailsList)
    {
        ProformaDetails temp=proformaDetailsList.get(0);
        int ind=0;
        int i=-1;
        for(ProformaDetails proformaDetails:proformaDetailsList)
        {
            i+=1;
            if(proformaDetails.getUnitPrice()<temp.getUnitPrice() && proformaDetails.getQuantity()!=0)
            {
                temp=proformaDetails;
                ind=i;
            }
        }
        return ind;
    }



    public List<Supplier> get_moins_disants(ConnexionBase connexionBase)
    {
        List<ProformaDetails> proformaDetailsList=get_proforma(connexionBase);
        List<Supplier> supplierList=new ArrayList<>();
        int ind;
        for (int i=0;i<proformaDetailsList.size();i++)
        {
            ind=get_min(proformaDetailsList);
            supplierList.add(proformaDetailsList.get(ind).getProforma().getSupplier());
            proformaDetailsList.remove(ind);
        }
        return supplierList;
    }

    List<ProformaDetails> get_proformat_moi_disants(ConnexionBase connexionBase)
    {
        List<ProformaDetails> proformaDetailsList=get_proforma(connexionBase);
        List<ProformaDetails> supplierList=new ArrayList<>();
        int ind;
        for (int i=0;i<proformaDetailsList.size();i++)
        {
            ind=get_min(proformaDetailsList);
            supplierList.add(proformaDetailsList.get(ind));
            proformaDetailsList.remove(ind);
        }
        return supplierList;
    }

    public List<OrderDetails> get_List_order(ConnexionBase connexionBase)
    {
        List<OrderDetails> orderDetailsList=new ArrayList<>();
        List<ProformaDetails> proformaDetailsList=get_proformat_moi_disants(connexionBase);
        int reste=this.getQuantity();
        OrderDetails orderDetails;
        for(ProformaDetails proformaDetails:proformaDetailsList)
        {
            if(reste!=0 && proformaDetails.getQuantity()!=0)
            {
                orderDetails=new OrderDetails();
                orderDetails.setProforma(proformaDetails.getProforma());
                orderDetails.setProduct(this.getProduct());
                orderDetails.setUnitPrice(proformaDetails.getUnitPrice());
                if(proformaDetails.getQuantity()>reste)
                {
                    orderDetails.setQuantity(reste);
                    reste=0;
                }
                else
                {
                    orderDetails.setQuantity(proformaDetails.getQuantity());
                    reste-=proformaDetails.getQuantity();
                }
                orderDetailsList.add(orderDetails);
                connexionBase.getOrderDetailsRepository().save(orderDetails);
            }
        }
        if(reste!=0)
        {
            throw  new RuntimeException("stock de fournisseurs insuffisant");
        }

        this.setState(4);
        connexionBase.getPurchaseRepository().save(this);
        return orderDetailsList;
    }

//    public void generer_bon_de_commande(ConnexionBase connexionBase)
//    {
//        List<OrderDetails> orderDetailsList=get_List_order(connexionBase);
//        for (OrderDetails orderDetails:orderDetailsList)
//        {
//            connexionBase.getOrderDetailsRepository().save(orderDetails);
//        }
//    }







}