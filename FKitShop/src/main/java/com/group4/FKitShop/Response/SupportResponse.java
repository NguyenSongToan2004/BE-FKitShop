package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Supporting;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportResponse {
    int maxSupportTimes;
    Supporting supporting;
}
