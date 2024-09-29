package com.group4.FKitShop.Exception;

public enum ErrorCode {
    UNCATEGORIZE(9999, "Uncategorized error"),
    INVALID_KEY(102, "Invalid message key"),
    LAB_NOTFOUND(101, "This lab is not exist !!"),
    LAB_NAMEDUPLICATED(102, "This lab name has taken alredy !!"),
    PRODUCT_NAMEDUPLICATED(103, "This product name has taken alredy !!"),
    PRODUCT_NOTFOUND(104, "This product is not exist !!"),
    UPLOAD_FILE_FAILED(105, "Fail to upload this file!!"),
    EMAIL_EXSITED("This email is already in use", 1001),
    UNCATEGORIZED_EXCEPTION("Uncategorized exception", 9999),
    USERNAME_INVALID("username at least 3 charaters", 1002),
    PHONE_EXISTED("phone number is already in use", 1003),
    USER_NOT_EXIST("wrong", 1004),
    PHONE_INVALID("Wrong format for phone number", 1005),
    UNAUTHENTICATED("User not existed", 1006);

    private int code = 100;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
