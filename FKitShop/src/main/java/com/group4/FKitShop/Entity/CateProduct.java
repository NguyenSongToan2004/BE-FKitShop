package com.group4.FKitShop.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "CateProduct")
public class CateProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cateProductID")
    int cateProductID;

    @Column(name = "productID")
    String productID;

    @Column(name = "categoryID")
    String categoryID;

}
