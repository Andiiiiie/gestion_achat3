package com.example.gestion_achat2.controller.global;

import com.example.gestion_achat2.entity.global.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {
    @GetMapping("/login")
    public String login(@ModelAttribute("user")User user) {
        return "global/user/login";
    }
}
