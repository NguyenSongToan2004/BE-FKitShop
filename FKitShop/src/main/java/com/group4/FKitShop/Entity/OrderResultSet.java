package com.group4.FKitShop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OrderResultSet {

    String email;
    String phoneNumber;
    String fullName;
    String ordersID;
    String productName;
    double price;
    int discount;
    int quantity;
    double discountPrice;
    double tmpPrice;

}
