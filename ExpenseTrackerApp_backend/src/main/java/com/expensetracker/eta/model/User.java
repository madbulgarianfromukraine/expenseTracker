package com.expensetracker.eta.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "auth_data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash")
    private String hashedPassword;

    @Column(name = "password_salt")
    private String randomSalt;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "dor", nullable = false)
    private LocalDate dateOfRegistration;

    // standard getters and setters

}