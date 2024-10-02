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
@Table(name = "Orders")
public class Orders {
    @Id
    @Column(name = "ordersID")
    String ordersID;

    @Column(name = "accountID")
    String accountID;

    @Column(name = "address")
    String address;

    @Column(name = "payingMethod")
    String payingMethod;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "totalPrice")
    Double totalPrice;

    @Column(name = "status")
    String status;

    @Column(name = "orderDate")
    @Temporal(TemporalType.DATE)
    Date orderDate;

    @Column(name = "shipDate")
    @Temporal(TemporalType.DATE)
    Date shipDate;


}
