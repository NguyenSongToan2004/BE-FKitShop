package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountsRequest {
    String accountID;
    @Size(min = 6, message = "Password at least 6 characters")
    String password;
    //String image;
    String fullName;
    Date dob;
    @Size(min = 10, message = "Phone number at 10 digits")
    String phoneNumber;
    String email;
    int status;
    String role;
    Date createDate;
    String adminID;
}
