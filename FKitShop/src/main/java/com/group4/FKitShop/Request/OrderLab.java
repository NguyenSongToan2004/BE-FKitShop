package com.group4.FKitShop.Request;

import com.group4.FKitShop.Entity.Lab;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderLab {
    String orderID;
    Lab lab;
}
