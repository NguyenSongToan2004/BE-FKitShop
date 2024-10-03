package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    String accountID;
    Map<String,@Min(value = 1, message = "Quantity must be greater than 0") Integer> productQuantity;
}
