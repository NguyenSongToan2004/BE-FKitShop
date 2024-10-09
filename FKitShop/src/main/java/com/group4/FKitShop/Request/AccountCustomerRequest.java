package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCustomerRequest {

    @Size(min = 6, message = "Password at least 6 characters")
    String password;
    String fullName;
    Date dob;
    @Size(min = 10, message = "Phone number at 10 digits")
    String phoneNumber;
    String email;
    

}
