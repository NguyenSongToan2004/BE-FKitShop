package com.group4.FKitShop.Request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportingRequest {
    String accountID;
    String orderDetailsID;
    String description;
}

