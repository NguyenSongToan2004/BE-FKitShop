package com.group4.FKitShop.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "OrderStatus")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderStatusID")
    int orderStatusID;

    @Column(name = "status")
    String status;
    // pending
    // in-progress
    // delivering
    // delivered

    @Column(name = "ordersID")
    String ordersID;

    @Column(name = "orderStatusDate")
    Date orderStatusDate;
}