package com.group4.FKitShop.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportingRequest {

    private String accountID;
    private String orderDetailsID;
    private int countdown;
    private int supportingCount;
    private int status;
}
