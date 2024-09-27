package com.group4.FKitShop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Accounts {
    @Id
    @NotNull
    String accountID;

    @NotNull
    String password;

    String image;

    @NotNull
    String fullName;

    @NotNull
    String email;

    @NotNull
    int status; // 0 : inactive ; 1: active

    @NotNull
    String role; // 0: admin ; 1: manager; 2: staff; 3: customer

    @NotNull
    Date createDate;
}
