package com.group4.FKitShop.Request;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrdersRequest {
    String accountID;
    String name;
    String province;
    String district;
    String ward;
    String address;
    String payingMethod;
    @Size(min = 10, message = "Phone number at 10 digits")
    String phoneNumber;
    Double shippingPrice;
    String note;
}
