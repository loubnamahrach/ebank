package com.example.ebankservice.model;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id
    private Long id;
    private String name;
    private String email;


}
