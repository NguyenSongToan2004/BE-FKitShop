package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    void toProduct(ProductRequest request, @MappingTarget Product product);

}
