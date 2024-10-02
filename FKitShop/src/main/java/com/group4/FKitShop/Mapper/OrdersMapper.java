package com.group4.FKitShop.Mapper;


import com.group4.FKitShop.Entity.Orders;
import com.group4.FKitShop.Request.OrdersRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    Orders toOrders(OrdersRequest request);
}
