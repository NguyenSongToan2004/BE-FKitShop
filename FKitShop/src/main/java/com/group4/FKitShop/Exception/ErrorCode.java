package com.group4.FKitShop.Exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
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
    // Tag
    TagName_DUPLICATED(1012, "This tag name has been taken"),
    Tag_NOTFOUND(1013, "Tag not found"),
    // Category
    Category_NOTFOUND(1014, "Category not found"),
    CategoryName_DUPLICATED(1015, "This category name has been taken"),
    // Blog
    Blog_DUPLICATED(1016, "This blog name has been taken"),
    Blog_NOTFOUND(1017, "Blog not found"),

    INVALID_TOKEN(1018, "Invalid token"),

    CateProduct_NOTFOUND(1019, "This relationship not found");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
