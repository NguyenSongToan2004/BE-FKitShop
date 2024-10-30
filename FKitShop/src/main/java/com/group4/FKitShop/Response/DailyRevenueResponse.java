package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyRevenueResponse {
     Date orderDate;
     Double dailyRevenue;
}
