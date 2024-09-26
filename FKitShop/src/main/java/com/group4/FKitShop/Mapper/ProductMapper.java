package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Request.ProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    public Product toProduct(ProductRequest request);
}
