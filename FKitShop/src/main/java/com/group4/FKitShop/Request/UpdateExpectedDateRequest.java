package com.group4.FKitShop.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateExpectedDateRequest {
    int supportingID;
    Date expectedDate;
}
