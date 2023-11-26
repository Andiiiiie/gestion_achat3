package com.example.gestion_achat2.controller.demande;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RequestController {
    @GetMapping("request/ask")
    public String demande()
    {
        return "";
    }
    @PostMapping("request/save")
    public String inserer()
    {
        return "";
    }
    @GetMapping("request/valider/{id}")
    public String valider_chef_service(@PathVariable Integer id)
    {
        return "";
    }
}
