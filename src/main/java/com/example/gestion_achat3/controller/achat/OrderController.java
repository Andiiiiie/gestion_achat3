package com.example.gestion_achat3.controller.achat;

import com.example.gestion_achat3.entity.commande.Order;
import com.example.gestion_achat3.entity.commande.OrderDetails;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.repository.*;
import com.example.gestion_achat3.service.ConnexionBase;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        model.addAttribute("orderList",orderList);
        return "order/list";
    }
    @GetMapping("order/validate/{id}")
    public String validate(@PathVariable Long id)
    {
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);
        Order order=orderRepository.findById(id).get();
        order.accepter(connexionBase);
        if(order.getState()==4)
        {
            return "redirect:/order/export/"+order.getId();
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
    public ResponseEntity<byte[]> exportProforma(HttpServletResponse response, @PathVariable Long id) throws DocumentException {
        Order orderConfirmation=orderRepository.findById(id).get();
        ConnexionBase connexionBase=new ConnexionBase(orderRepository,orderDetailsRepository,userRepository,productRepository,purchaseRepository,requestRepository,request_typeRepository,serviceRepository,supplierRepository,proformaRepository,proformaDetailsRepository);

        // Create a new document
        Document document = new Document();

        // Create a new ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a new PdfWriter
        PdfWriter.getInstance(document,byteArrayOutputStream);

        // Open the document
        document.open();

        // Add a title to the document
        Paragraph title = new Paragraph("BON DE COMMANDE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25));
        document.add(title);


        document.add(new Paragraph("Fournisseur",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));
        PdfPTable table=new PdfPTable(2);
        table.addCell("Nom:");
        table.addCell(orderConfirmation.getSupplier().getName());
        table.addCell("Contact:");
        table.addCell(orderConfirmation.getSupplier().getContact());
        table.addCell("adresse:");
        table.addCell(orderConfirmation.getSupplier().getAdresse());
        table.addCell("Responsable");
        table.addCell(orderConfirmation.getSupplier().getResponsable());

        document.add(table);


        document.add(new Paragraph("DETAILS ACHAT ",FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));

        PdfPTable table1=new PdfPTable(3);
        table1.addCell("Produit");
        table1.addCell("Quantite");
        table1.addCell("Prix unitaire ");


        List<OrderDetails> orderDetailsList=orderDetailsRepository.findByOrder(orderConfirmation);
        for(OrderDetails orderDetails:orderDetailsList)
        {
            table1.addCell(orderDetails.getProduct().getDesignation());
            table1.addCell(orderDetails.getQuantity().toString());
            table1.addCell(orderDetails.getUnitPrice().toString());
        }
        document.add(table1);

        PdfPTable table2=new PdfPTable(2);
        table2.addCell("Prix HT"); table2.addCell(orderConfirmation.getPrix_ht().toString());
        table2.addCell("tva");table2.addCell(orderConfirmation.getTva().toString());
        table2.addCell("PrixTTC");table2.addCell(orderConfirmation.getPrix_ttc().toString());

        document.add(table2);
        document.close();

        // Convert the ByteArrayOutputStream to a byte array
        byte[] bytes = byteArrayOutputStream.toByteArray();

        // Create the HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.pdf");

        // Return the byte array as a ResponseEntity
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
