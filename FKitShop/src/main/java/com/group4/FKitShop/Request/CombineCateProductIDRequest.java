package com.group4.FKitShop.Request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CombineCateProductIDRequest {

        CategoryRequest categoryRequest;
        ProductIDRequest productIDRequest;
}
