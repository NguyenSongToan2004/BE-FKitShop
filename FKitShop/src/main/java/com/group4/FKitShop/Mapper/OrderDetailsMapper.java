package com.group4.FKitShop.Mapper;


import com.group4.FKitShop.Entity.OrderDetails;
import com.group4.FKitShop.Request.OrderDetailsRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {
    OrderDetails toOrderDetails(OrderDetailsRequest request);
}
