package com.example.gestion_achat3.controller.achat;

import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.entity.fournisseur.Proforma;
import com.example.gestion_achat3.entity.fournisseur.Supplier;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
import com.example.gestion_achat3.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
@Controller
public class SupplierController {
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
    ProformaDetailsRepository proformaDetailsRepository;

    public SupplierController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository, ProformaDetailsRepository proformaDetailsRepository) {
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




    @GetMapping("supplier/demander/{id}")
    public String got_to_formulaire(Model model, @PathVariable int id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Supplier supplier=supplierRepository.findById(id).get();
        List<Purchase> purchaseList=supplier.get_achats_sans_demande(connexionBase);
        model.addAttribute("purchaseList",purchaseList);
        model.addAttribute("supplier",supplier);
        //mila fantatra ilay id anle profornma mila fenoina
        return "purchase/demande";
    }

    @GetMapping("supplier/list")
    public String get_List(Model model)
    {
        List<Supplier> supplierList=supplierRepository.findAll();
        model.addAttribute("supplierList",supplierList);
        return "purchase/demande_proforma";
    }


    @PostMapping("supplier/send")
    @Transactional
    public String envoyer_demande(@RequestParam(value = "ids", required = true) Integer[] ids, @RequestParam(value = "supplier", required = true) Integer supplier, RedirectAttributes redirectAttributes) throws GeneralSecurityException, IOException, MessagingException {
        List<Purchase> purchaseList=new ArrayList<>();
        for (int i=0;i<ids.length;i++)
        {
            purchaseList.add(purchaseRepository.findById(ids[i]).get());
        }
        Supplier supplier1=supplierRepository.findById(supplier).get();
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Proforma proforma = supplier1.demande_proformat(connexionBase,purchaseList);

        try {
            MailService mailService = new MailService();
            String destinataire = "mendrika261@icloud.com";
            mailService.sendProforma(destinataire, MailService.HOST + "proformat/form/" + proforma.getId());
            redirectAttributes.addFlashAttribute("successData", "Un email a été envoyé à " + destinataire);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorData", "Une erreur s'est produite lors de l'envoi de l'email");
        }

        return "redirect:/supplier/list";
    }
    @GetMapping("supplier/order_list/{id}")
    public String list_order(@PathVariable int id,Model model)
    {
        Supplier supplier=supplierRepository.findById(id).get();
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        List<OrderDetails> orderDetailsList=supplier.get_orders(connexionBase);
        model.addAttribute("orderDetailsList",orderDetailsList);
        model.addAttribute("supplier",supplier);
        return "supplier/order";
    }

    @GetMapping("supplier/generate_order/{id}")
    public String generate_order(@PathVariable int id,Model model)
    {
        Supplier supplier=supplierRepository.findById(id).get();
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        supplier.get_bon_de_commande(connexionBase);
        return "redirect:/supplier/list";
    }
}
