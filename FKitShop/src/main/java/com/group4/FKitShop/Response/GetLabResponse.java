package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetLabResponse {
    String labID;
    String productID;
    String productName;
    String name;
    String description;
    String level;
    String createDate;
    String fileNamePDF;
}
