package com.group4.FKitShop.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringRespone {
    int status = 1000;
    String message;
    List<String> strRespone;
}
