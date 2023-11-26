package com.example.gestion_achat3.entity.global;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "roles")
    private String roles;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;


    public boolean is_role(String r)
    {
        String maChaine = this.getRoles();
        String[] valeurs = maChaine.split(",");
        List<String> maListe=new ArrayList<>();
        for(int i=0;i<valeurs.length;i++)
        {
            maListe.add(valeurs[i]);
        }
        if (maListe.contains(r)) {
            return true;
        } else {
            return false;
        }

    }

}