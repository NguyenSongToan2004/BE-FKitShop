package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Category;
import com.group4.FKitShop.Entity.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagResponse {

    Tag tag;
    List<Category> cates;
}
