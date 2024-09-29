package com.group4.FKitShop.Exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    EMAIL_EXSITED(1001, "This email is already in use"),
    USERNAME_INVALID(1002, "username at least 3 charaters"),
    PHONE_EXISTED(1003, "phone number is already in use"),
    USER_NOT_EXIST(1004, "User Not Found"),
    PHONE_INVALID(1005, "Wrong format for phone number"),
    UNAUTHENTICATED(1006, "User not existed"),
    // Lab
    LAB_NOTFOUND(1007, "This lab is not exist !!"),
    LAB_NAMEDUPLICATED(1008, "This lab name has taken alredy !!"),
    // Product
    PRODUCT_NAMEDUPLICATED(1009, "This product name has taken alredy !!"),
    PRODUCT_NOTFOUND(1010, "This product is not exist !!"),
    // File
    UPLOAD_FILE_FAILED(1011, "Fail to upload this file!!"),

    TagName_DUPLICATED(1001, "This tag name has been taken"),
    CategoryName_DUPLICATED(1001, "This category name has been taken"),
    Blog_DUPLICATED(1001, "This blog name has been taken"),

    Tag_NOTFOUND(1002, "Tag not found"),
    Category_NOTFOUND(1002, "Category not found"),
    Blog_NOTFOUND(1002, "Blog not found")
    ;

    private int code = 1000;
    private String message;
    
}
