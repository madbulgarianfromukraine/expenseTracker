package com.expensetracker.eta.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String hashedPassword;

    private byte[] randomSalt;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "dor", nullable = false)
    private LocalDate dateOfRegistration;

    // standard getters and setters

}