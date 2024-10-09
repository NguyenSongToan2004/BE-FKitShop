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
    UNAUTHENTICATED(1006, "Email or Password is incorrect"),
    // Lab
    LAB_NOTFOUND(1007, "This lab is not exist !!"),
    LAB_NAMEDUPLICATED(1008, "This lab name has taken alredy !!"),
    LAB_UPLOAD_FAILED(1018, "Upload failed"),
    LAB_UNSUPPORTED_FILENAME(1019, "Unsupported filename !!"),
    LAB_DOWNLOAD_FAILED(1020, "Download failed"),
    // Product
    PRODUCT_NAMEDUPLICATED(1009, "This product name has taken alredy !!"),
    PRODUCT_NOTFOUND(1010, "This product is not exist !!"),
    PRODUCT_UNAVAILABLE(1234, "This product is out of stock"),
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
    //token
    INVALID_TOKEN(1018, "Invalid token"),
    //sql
    EXECUTED_FAILED(1111, "Executed failed"),
    //cart
    CART_NOTFOUND(1100, "Cart not found"),
    //orders
    ORDERS_NOTFOUND(1019, "Orders not found"),
    ORDER_CREATION_FAILED(1020, "Order creation failed"),
    //orderdetails
    ORDERDETAILS_NOTFOUND(1021, "Order details not found"),
    //Supporting
    SUPPORTING_NOT_FOUND(1022, "Support not found"),
    SUPPORTING_LIMITED(1023, "Support out of limit !!"),
    SUPPORTING_UNSUPPORTED_STATUS_CODE(1024, "Unsupported status code !!"),
    SUPPORTING_INVALID_SUPPORT_DATE(1025, "Invalid support date !!"),
    //order status
    OrderStatus_NOTFOUND(1022, "Order status not found"),
    OrderStatus_EXIST(1023, "Order status already exist"),
    ORDER_FAILED(1234, "Order failed");




    // max = 18
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
