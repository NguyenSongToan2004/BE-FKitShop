package com.group4.FKitShop.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountsResponse {
    String accountID;
    String image;
    String fullName;
    Date dob;
    String phoneNumber;
    String email;
    int status;
    String role;
    Date createDate;
    String adminID;
}
