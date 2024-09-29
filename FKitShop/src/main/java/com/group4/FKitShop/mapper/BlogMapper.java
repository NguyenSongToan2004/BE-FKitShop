package com.group4.FKitShop.mapper;



import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Request.BlogRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface BlogMapper {

    Blog toBlog(BlogRequest request);
}
