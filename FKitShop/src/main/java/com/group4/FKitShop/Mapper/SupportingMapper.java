package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Supporting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupportingMapper {

    @Mapping(source = "description", target = "description", ignore = true)
    @Mapping(source = "status", target = "status", ignore = true)
    @Mapping(source = "postDate", target = "postDate", ignore = true)
    @Mapping(source = "supportDate", target = "supportDate", ignore = true)
    @Mapping(source = "supportingID" , target = "supportingID", ignore = true)
    Supporting toSupporting(Supporting supporting);
}
