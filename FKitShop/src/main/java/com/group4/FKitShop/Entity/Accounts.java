package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "accountID")
    private String accountID;

    @Column(name = "password")
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "yob")
    private int yob;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "status", columnDefinition = "integer default 1")
    private int status;

    @Column(name = "role", columnDefinition = "varchar default user")
    private String role;

    @Column(name = "createDate")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Column(name = "managerID")
    private String managerId;





}
