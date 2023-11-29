package com.example.gestion_achat3;

import com.example.gestion_achat3.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootTest
class GestionAchat3ApplicationTests {

    @Test
    void contextLoads() throws GeneralSecurityException, IOException, MessagingException {
        MailService mailService = new MailService();
        mailService.sendProforma("mendrika261@icloud.com", "http://localhost:8080");
    }

}
