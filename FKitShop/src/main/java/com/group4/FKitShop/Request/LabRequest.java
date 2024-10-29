package com.group4.FKitShop.Request;

import com.group4.FKitShop.Entity.Lab;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabRequest {
    String productID;
    String name;
    String description;
    String level;
    int status;
}
