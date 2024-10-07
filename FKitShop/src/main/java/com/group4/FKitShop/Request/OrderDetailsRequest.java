package com.group4.FKitShop.Request;


import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailsRequest {
    Map<String,@Min(value = 1, message = "Quantity must be greater than 0") Integer> productQuantity;
}