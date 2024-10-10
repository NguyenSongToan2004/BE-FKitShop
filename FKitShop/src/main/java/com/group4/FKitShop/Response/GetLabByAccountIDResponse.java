package com.group4.FKitShop.Response;

import com.group4.FKitShop.Request.OrderLab;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetLabByAccountIDResponse {
    String accountID;
    Set<OrderLab> orderLabs;
}
