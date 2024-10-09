package com.group4.FKitShop.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceFeeRequest {
    int to_district_id;
    int to_ward_code;
    int service_cost;
}
