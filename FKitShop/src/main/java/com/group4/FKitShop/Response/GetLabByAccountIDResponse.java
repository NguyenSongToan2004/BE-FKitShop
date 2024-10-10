package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Lab;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetLabByAccountIDResponse {
    List<Lab> labs;
}
