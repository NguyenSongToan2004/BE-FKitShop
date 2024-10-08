package com.group4.FKitShop.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartID")
    int cartID;
    @Column(name = "accountID")
    String accountID;
    @Column(name = "productID")
    String productID;
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Column(name = "quantity")
    int quantity;
    @Column(name = "status")
    String status;

}
