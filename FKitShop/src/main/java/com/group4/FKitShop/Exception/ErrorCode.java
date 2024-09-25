package com.group4.FKitShop.Exception;

public enum ErrorCode {
    UNCATEGORIZE(9999,"Uncategorized error"),
    INVALID_KEY(102, "Invalid message key"),
    LAB_NOTFOUND(101, "This lab is not exist !!"),
    LAB_NAMEDUPLICATED(102, "This lab name has taken alredy !!"),
    PRODUCT_NAMEDUPLICATED(103, "This product name has taken alredy !!"),
    PRODUCT_NOTFOUND(104, "This product is not exist !!"),
    UPLOAD_FILE_FAILED(105, "Fail to upload this file!!")

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
