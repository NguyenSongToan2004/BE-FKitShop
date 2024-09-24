package com.group4.FKitShop.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AccountsRequest {
    private String accountID;
    private String password;
    private String image;
    private String fullName;
    private int yob;
    private String phoneNumber;
    private String email;
    private int status;
    private String role;
    private Date createDate;
    private String managerId;
}
