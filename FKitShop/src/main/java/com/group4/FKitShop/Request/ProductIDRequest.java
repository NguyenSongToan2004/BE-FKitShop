package com.group4.FKitShop.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductIDRequest {
    List<String> productID;
}
