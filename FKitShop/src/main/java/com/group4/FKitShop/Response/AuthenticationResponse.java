package com.group4.FKitShop.Response;


import com.group4.FKitShop.Entity.Accounts;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    Accounts accounts;
    boolean isAutheticated;
    String token;
}
