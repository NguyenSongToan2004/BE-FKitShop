package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.Wishlist;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistResponse {
    Wishlist wishlist;
    String customerName;
    Product products;
}
