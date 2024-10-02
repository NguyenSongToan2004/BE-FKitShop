package com.group4.FKitShop.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersResponse {

    String accountID;
    String address;
    String payingMethod;
    String phoneNumber;
    Double totalPrice;
    String status;
    Date orderDate;
    Date shipDate;
}
