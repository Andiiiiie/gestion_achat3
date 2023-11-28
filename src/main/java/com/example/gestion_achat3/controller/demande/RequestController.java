package com.example.gestion_achat3.controller.demande;

import com.example.gestion_achat3.entity.achat.Product;
import com.example.gestion_achat3.entity.achat.Request;
import com.example.gestion_achat3.entity.achat.Request_type;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RequestController {
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

    public RequestController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository,
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

    @GetMapping("request/ask")
    public String demande(@ModelAttribute Request request, Model model)
    {
        List<Product> productList = productRepository.findAll();
        List<Request_type> request_typeList = request_typeRepository.findAll();
        model.addAttribute("productList", productList);
        model.addAttribute("request_typeList", request_typeList);
        return "request/insert";
    }

    @GetMapping("request/list_chef")
    public String list_demandes_chef(@ModelAttribute Request request,Model model)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        User user=connexionBase.get_connected();
        List<Request> requestList=requestRepository.findByStateAndUser_requesterService(0, user.getService());
        model.addAttribute("requestList",requestList);
        model.addAttribute("service", user.getService());
        return "request/list_chef";
    }


    @GetMapping("request/list_achat")
    public String list_demandes_achat(@ModelAttribute Request request,Model model)
    {
        List<Request> requestList=requestRepository.findByState(1);
        model.addAttribute("requestList",requestList);
        return "request/list";
    }


    @GetMapping("request/validate/{id}")
    public String validate(Model model,@PathVariable Integer id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Request request=requestRepository.findById(id).get();
        if(request.valider(connexionBase)==1)
        {
            return "redirect:/request/list_chef";
        }
        else
        {
            return "redirect:/request/list_achat";
        }
    }

    @GetMapping("request/refuse/{id}")
    public String refuse(Model model,@PathVariable Integer id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Request request=requestRepository.findById(id).get();
        if(request.refuser(connexionBase)==1)
        {
            return "redirect:/request/list_chef";
        }
        else
        {
            return "redirect:/request/list_achat";
        }
    }
    @PostMapping("request/save")
    public String inserer(@ModelAttribute Request request, Model model, RedirectAttributes redirectAttributes)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        try {
            request.demander(connexionBase);
        }catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/request/ask";
        }
        return "redirect:/request/ask";
    }
    @GetMapping("request/valider/{id}")
    public String valider_chef_service(@PathVariable Integer id)
    {
        return "";
    }
}
