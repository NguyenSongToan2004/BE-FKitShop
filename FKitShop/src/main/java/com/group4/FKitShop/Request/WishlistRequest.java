package com.group4.FKitShop.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistRequest {

    private String accountID;
    private String productID;


}
