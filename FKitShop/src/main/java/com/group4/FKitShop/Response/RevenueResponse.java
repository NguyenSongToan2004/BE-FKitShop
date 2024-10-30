package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueResponse {
    String monthCode;
    Double totalProductPrice;
    Double totalShippingPrice;
    Double totalRevenue;
    Double differenceRevenue;
    Double differencePercent;
    int status;
}
