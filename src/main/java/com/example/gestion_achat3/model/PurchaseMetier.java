package com.example.gestion_achat3.model;

import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.achat.Request;
import com.example.gestion_achat3.service.ConnexionBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class PurchaseMetier {
    Purchase purchase;
    List<Request> requestList;


    public void valider(ConnexionBase connexionBase)
    {
        this.getPurchase().setState(0);
        connexionBase.getPurchaseRepository().save(this.getPurchase());
        for(Request request:this.getRequestList())
        {
            request.setPurchase(this.getPurchase());
            connexionBase.getRequestRepository().save(request);
        }
    }

}
