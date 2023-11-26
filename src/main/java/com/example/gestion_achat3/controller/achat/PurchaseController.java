package com.example.gestion_achat3.controller.achat;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.model.PurchaseMetier;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PurchaseController {
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

    public PurchaseController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository,
                              ProformaDetailsRepository proformaDetailsRepository) {
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


    @GetMapping("purchase/list")
    public String get_List_attente(Model model)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        List<PurchaseMetier> purchaseMetierList=Purchase.get_list(connexionBase);
        model.addAttribute("purchaseMetierList",purchaseMetierList);
        return "purchase/list";
    }
    @GetMapping("purchase/details/{id}")
    public String get_details(Model model, @PathVariable int id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Product product=productRepository.findById(id).get();
        PurchaseMetier purchaseMetier=product.get_achat(connexionBase);
        model.addAttribute("purchase",purchaseMetier);
        return "purchase/details";
    }

    @GetMapping("purchase/validate/{id}")
    public String valider(@PathVariable int id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Product product=productRepository.findById(id).get();
        PurchaseMetier purchaseMetier=product.get_achat(connexionBase);
        purchaseMetier.valider(connexionBase);
        return "redirect:/purchase/list";
    }

    @GetMapping("purchase/list_valid")
    public String list_avec_proforma(Model model)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        List<Purchase> purchaseList=Purchase.get_purchase_with_3_proforma(connexionBase);
        model.addAttribute("purchaseList",purchaseList);
        return "purchase/list_valid";
    }

    @GetMapping("purchase/generate_order/{id}")
    public String generateOrders(@PathVariable int id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Purchase purchase=purchaseRepository.findById(id).get();
        purchase.get_List_order(connexionBase);
        return "redirect:/purchase/list_valid";
    }
}
