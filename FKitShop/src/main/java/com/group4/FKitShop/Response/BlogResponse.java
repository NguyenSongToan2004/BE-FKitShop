package com.group4.FKitShop.Response;


import com.group4.FKitShop.Entity.Blog;
import com.group4.FKitShop.Entity.BlogTag;
import com.group4.FKitShop.Entity.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    Blog blog;
    List<Tag> tags;
}
