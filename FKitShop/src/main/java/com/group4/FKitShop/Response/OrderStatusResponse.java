package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.OrderStatus;
import com.group4.FKitShop.Entity.Orders;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {
    Orders order;
    List<OrderStatus> orderStatus;
}