package com.group4.FKitShop.Exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_EXSITED("This email is already in use", 1001),
    UNCATEGORIZED_EXCEPTION("Uncategorized exception", 9999),
    USERNAME_INVALID("username at least 3 charaters", 1002),
    USER_NOT_EXIST("wrong", 1003),
    PHONE_INVALID("Wrong format for phone number", 1004)
    ;
    private String message;
    private int code;


}
