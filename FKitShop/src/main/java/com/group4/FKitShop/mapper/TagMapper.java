package com.group4.FKitShop.mapper;


import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Request.TagRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toTag(TagRequest request);
}
