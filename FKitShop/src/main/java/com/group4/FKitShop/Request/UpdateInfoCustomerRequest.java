package com.group4.FKitShop.Request;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoCustomerRequest {

    String fullName;
    @Temporal(TemporalType.DATE)
    Date dob;
    String phoneNumber;
    String email;

}
