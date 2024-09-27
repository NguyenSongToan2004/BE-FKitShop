package com.group4.FKitShop.Request;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateInfoCustomerRequest {
    
    String fullName;
    Date dob;
    String phoneNumber;
    String email;
    
}
