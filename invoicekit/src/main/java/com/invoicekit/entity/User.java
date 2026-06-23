package com.invoicekit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String companyName;

    private String gstNumber;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private String bankName;

    @Column(nullable = true)
    private String accountNumber;

    @Column(nullable = true)
    private String ifscCode;

    @Column(nullable = true)
    private String accountHolderName;

    @Column(nullable = true)
    private String upiId;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Client> clients;
}