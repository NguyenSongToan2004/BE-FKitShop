package com.group4.FKitShop.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Accounts")
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "accountID")
    String accountID;

    @Column(name = "password")
    String password;

    @Column(name = "image")
    String image;

    @Column(name = "fullName")
    String fullName;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    Date dob;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "email")
    String email;

    @Column(name = "status", columnDefinition = "integer default 0")
    int status;

    @Column(name = "role", columnDefinition = "varchar default user")
    String role;

    @Column(name = "createDate")
    @Temporal(TemporalType.DATE)
    Date createDate;

    @Column(name = "adminID")
    String adminID;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private Set<Supporting> supportings = new HashSet<>();

}
