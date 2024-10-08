package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Cart;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Response.ProductCartResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CartMapper {
    ProductCartResponse toProductCartResponse(Cart cart);
}
