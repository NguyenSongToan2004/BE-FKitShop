package com.group4.FKitShop.Mapper;


import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Request.CategoryRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
}
