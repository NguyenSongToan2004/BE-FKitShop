package com.group4.FKitShop.Response.SubResponse;

import com.group4.FKitShop.Entity.Supporting;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AllLabSupport {
    String accountID;
    String customerName;
    String labID;
    String labName;
    Supporting supporting;
}
