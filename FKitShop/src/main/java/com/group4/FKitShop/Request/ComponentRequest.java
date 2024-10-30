package com.group4.FKitShop.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComponentRequest {
    HashMap<String,Integer> components;
}