package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueResponse {
    String code;
    BigDecimal totalProductPrice;
    BigDecimal totalShippingPrice;
    BigDecimal totalRevenue;
    BigDecimal differenceRevenue;
    Double differencePercent;
    int status;
}
