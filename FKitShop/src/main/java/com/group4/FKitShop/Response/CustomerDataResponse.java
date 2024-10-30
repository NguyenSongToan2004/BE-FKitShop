package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDataResponse {
    String accountID;
    String accountName;
    int numberOrder;
}
