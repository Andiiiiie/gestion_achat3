package com.example.gestion_achat3.controller.achat;

import com.example.gestion_achat3.entity.commande.Order;
import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
import com.example.gestion_achat3.service.MailService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Getter
@Setter
@Controller
public class OrderController {
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

    public OrderController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository, ProformaDetailsRepository proformaDetailsRepository) {
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


    @GetMapping("order/list")
    public String list_orders(Model model)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        User user=connexionBase.get_connected();
        List<Order> orderList=null;
        //si achat
        if(user.getService().getName().equals("ACHAT"))
        {
            orderList=orderRepository.findByState(0);
        }
        //si finance
        if(user.getService().getName().equals("FINANCE"))
        {
            orderList=orderRepository.findByState(1);
        }
        //si DAF
        if(user.getService().getName().equals("DAF"))
        {
            orderList=orderRepository.findByState(2);
        }
        //si DG
        if(user.getService().getName().equals("DG"))
        {
            orderList=orderRepository.findByState(3);
        }
        model.addAttribute("orderList", orderList);
        return "order/list";
    }
    @GetMapping("order/validate/{id}")
    @Transactional
    public String validate(@PathVariable Long id, RedirectAttributes redirectAttributes) throws GeneralSecurityException, IOException, MessagingException {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Order order=orderRepository.findById(id).get();
        order.accepter(connexionBase);
        if(order.getState()==4)
        {
            try {
                MailService mailService = new MailService();
                String destinataire = "mendrika261@icloud.com";
                mailService.sendBonDeCommande(destinataire, MailService.HOST + "order/export/" + order.getId());
                redirectAttributes.addFlashAttribute("successData", new String[]{"Un email a été envoyé à " + destinataire});
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorData", new String[]{"Vérifier votre connexion internet"});
            }
        }
        return "redirect:/order/list";
    }


    @GetMapping("order/refuse/{id}")
    public String refuse(@PathVariable Long id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Order order=orderRepository.findById(id).get();
        order.refuser(connexionBase);
        return "redirect:/order/list";
    }

    @GetMapping("order/details/{id}")
    public String details(@PathVariable Long id,Model model)
    {
        Order order=orderRepository.findById(id).get();
        List<OrderDetails> orderDetailsList=orderDetailsRepository.findByOrder(order);
        model.addAttribute("order",order);
        model.addAttribute("orderDetailsList",orderDetailsList);
        return "order/details";
    }


    @GetMapping("order/export/{id}")
    public String exportProforma(@PathVariable Long id, Model model) {
        Order order=orderRepository.findById(id).get();
        List<OrderDetails> orderDetailsList=orderDetailsRepository.findByOrder(order);
        model.addAttribute("order",order);
        model.addAttribute("orderDetailsList",orderDetailsList);
        return "order/pdf";
    }
}
