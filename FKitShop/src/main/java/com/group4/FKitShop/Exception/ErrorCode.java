package com.group4.FKitShop.Exception;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    EMAIL_EXSITED("This email is already in use", 1001),
    UNCATEGORIZED_EXCEPTION("Uncategorized exception", 9999),
    USERNAME_INVALID("username at least 3 charaters", 1002),
    PHONE_EXISTED("phone number is already in use", 1003),
    USER_NOT_EXIST("wrong", 1004),
    PHONE_INVALID("Wrong format for phone number", 1005),
    UNAUTHENTICATED("User not existed", 1006)
    ;
    String message;
    int code;


}
