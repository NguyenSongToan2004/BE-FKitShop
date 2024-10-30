package com.group4.FKitShop.Response;


import com.group4.FKitShop.Entity.CateProduct;
import com.group4.FKitShop.Entity.Component;
import com.group4.FKitShop.Entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductResponse {
    Product product;
    List<CateProduct> cp;
}
