package com.example.gestion_achat3.controller.achat;

import com.example.gestion_achat3.entity.fournisseur.Proforma;
import com.example.gestion_achat3.entity.fournisseur.ProformaDetails;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProformatController {
    OrderRepository orderRepository;
    OrderDetailsRepository orderDetailsRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    PurchaseRepository purchaseRepository;
    RequestRepository requestRepository;
    Request_typeRepository request_typeRepository;
    ServiceRepository serviceRepository;
    SupplierRepository supplierRepository;

    ProformaRepository proformaRepository;
    private final ProformaDetailsRepository proformaDetailsRepository;

    public ProformatController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository, ProformaDetailsRepository proformaDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.requestRepository = requestRepository;
        this.request_typeRepository = request_typeRepository;
        this.serviceRepository = serviceRepository;
        this.supplierRepository = supplierRepository;
        this.proformaRepository = proformaRepository;
        this.proformaDetailsRepository = proformaDetailsRepository;
    }

    @GetMapping("proformat/form/{id}")
    public String formulaire( Model model, @PathVariable int id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Proforma proforma=proformaRepository.findById(id).get();
        if(proforma.getState()==0)
        {
            List<ProformaDetails> proformaDetailsList=proforma.get_proforma(connexionBase);
            model.addAttribute("proformaDetailsList",proformaDetailsList);
            model.addAttribute("proforma", proforma);
            return "fournisseur/proforma/insert";
        }
        else
        {
            model.addAttribute("message","page indisponible");
            return "redirect:/error";
        }
    }

    @PostMapping("proformat/save")
    public String enregister(@RequestParam(value = "id[]") Long[] id,@RequestParam(value = "prix[]") double[] prix,@RequestParam(value = "quantite[]") int[] quantite)
    {
        for (int i=0;i<id.length;i++)
        {
            ProformaDetails proformaDetails=proformaDetailsRepository.findById(id[i]).get();
            if(proformaDetails.getPurchase().getNbrProforma()!=null)
            {
                proformaDetails.getPurchase().setNbrProforma(proformaDetails.getPurchase().getNbrProforma()+1);
            }
            else
            {
                proformaDetails.getPurchase().setNbrProforma(1);
            }

            purchaseRepository.save(proformaDetails.getPurchase());
            proformaDetails.setQuantity(quantite[i]);
            proformaDetails.setUnitPrice(prix[i]);
            proformaDetails.getProforma().setState(1);
            proformaDetails.getProforma().setDate_reception(LocalDate.now());
            proformaDetailsRepository.save(proformaDetails);
            proformaRepository.save(proformaDetails.getProforma());
        }
        return "fournisseur/proforma/success";
    }
}
