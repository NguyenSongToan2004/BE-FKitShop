package com.group4.FKitShop.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String image;
    String description;
    String publisher;
    int quantity;
    double price;
    int discount;
    String status;
    double weight;
    String material;
    String dimension;
    String type;
    // ko có ngày tạo vì sẽ tự tạo
}
