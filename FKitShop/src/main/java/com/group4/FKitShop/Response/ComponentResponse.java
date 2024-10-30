package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComponentResponse {
    int componentIndex;
    String componentID;
    String componentName;
    int quantity;
}