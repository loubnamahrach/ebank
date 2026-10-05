package com.example.ebankservice.entities;

import com.example.ebankservice.model.Customer;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {

    @Id
    private String id;
    private Date createdAt;
    private double solde;
    private String type;
    private long customerId;
    private double balance;


    @Transient
    private Customer customer;
}
