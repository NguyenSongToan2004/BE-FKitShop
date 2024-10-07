package com.group4.FKitShop.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutRequest {
    OrdersRequest ordersRequest;
    OrderDetailsRequest orderDetailsRequest;
}
