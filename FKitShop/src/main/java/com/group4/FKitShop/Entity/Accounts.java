package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


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
    Date dob;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "email")
    String email;

    @Column(name = "status", columnDefinition = "integer default 1")
    int status;

    @Column(name = "role", columnDefinition = "varchar default user")
    String role;

    @Column(name = "createDate")
    @Temporal(TemporalType.DATE)
    Date createDate;

    @Column(name = "managerID")
    String managerId;

}
