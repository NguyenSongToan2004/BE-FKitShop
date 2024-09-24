package com.group4.FKitShop.Exception;

public enum ErrorCode {
    UNCATEGORIZE(9999,"Uncategorized error"),
    INVALID_KEY(102, "Invalid message key"),
    LAB_NOTFOUND(101, "This lab is not exist !!"),
    LAB_NAMEDUPLICATED(102, "This lab name has taken alredy !!")
    ;
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
