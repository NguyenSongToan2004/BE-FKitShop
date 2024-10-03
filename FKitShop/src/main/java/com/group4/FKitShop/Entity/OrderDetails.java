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
@Table(name = "OrderDetails")
public class OrderDetails {

    @Id
    @Column(name = "orderDetailsID")
    String orderDetailsID;
    @Column(name = "productID")
    String productID;
    @Column(name = "ordersID")
    String ordersID;
    @Column(name = "quantity")
    int quantity;
    @Column(name = "price")
    double price;
    @Column(name = "isActive")
    int isActive;
    @Column(name = "status")
    String status;
    @Column(name = "confirmDate")
    @Temporal(TemporalType.DATE)
    Date confirmDate;
    @Column(name = "warrantyDate")
    @Temporal(TemporalType.DATE)
    Date warrantyDate;

}
