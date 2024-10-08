package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Category cate;
    List<CateProduct> cp;
}
