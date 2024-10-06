package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Request.BlogRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(source = "tagID", target = "tagID", ignore = true)
    Blog toBlog(BlogRequest request);

}
