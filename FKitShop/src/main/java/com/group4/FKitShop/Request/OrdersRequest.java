package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrdersRequest {
    String accountID;
    String address;
    String payingMethod;
    @Size(min = 10, message = "Phone number at 10 digits")
    String phoneNumber;
    Double shippingPrice;

}
