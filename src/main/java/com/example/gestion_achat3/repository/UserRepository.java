package com.example.gestion_achat3.repository;

import com.example.gestion_achat3.entity.global.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
}