package com.example.gestion_achat3.service;

import com.example.gestion_achat3.config.CustomUserDetails;
import com.example.gestion_achat3.entity.achat.Purchase;
import com.example.gestion_achat3.entity.fournisseur.ProformaDetails;
import com.example.gestion_achat3.entity.global.User;
import com.example.gestion_achat3.repository.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Setter
@Getter
@Service
public class ConnexionBase {
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

    public ConnexionBase(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, RequestRepository requestRepository, Request_typeRepository request_typeRepository, ServiceRepository serviceRepository, SupplierRepository supplierRepository, ProformaRepository proformaRepository, ProformaDetailsRepository proformaDetailsRepository) {
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

    public User get_connected()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails cUser = (CustomUserDetails) authentication.getPrincipal();
        User user = cUser.user;
        return user;
    }
}
