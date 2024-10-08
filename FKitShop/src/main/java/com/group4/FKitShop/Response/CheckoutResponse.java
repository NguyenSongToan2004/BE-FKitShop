package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Request.OrdersRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutResponse {
    Orders orders;
    List<OrderDetails> orderDetails;


}
