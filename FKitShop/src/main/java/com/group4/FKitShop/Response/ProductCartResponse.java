package com.group4.FKitShop.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCartResponse {
    String productID;
    String name;
    String image;
    double price;
    int quantity;
    String status;
}