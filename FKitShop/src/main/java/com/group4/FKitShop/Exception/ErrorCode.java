package com.group4.FKitShop.Exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    // User / Authentication Errors
    EMAIL_EXSITED(1001, "This email is already in use"),
    USERNAME_INVALID(1002, "Username must be at least 6 characters"),
    PHONE_EXISTED(1003, "Phone number is already in use"),
    PHONE_ERROR(1004, "Phone number must start with 0 and contain 10 digits"),
    USER_NOT_EXIST(1005, "User Not Found"),
    PHONE_INVALID(1006, "Wrong format for phone number"),
    UNAUTHENTICATED(1007, "Email or Password is incorrect"),
    USER_PASS_LENGHT(1008, "Password must be at least 6 characters"),
    USER_PASS_INCORECT(1009, "Incorrect current password"),

    // Lab Errors
    LAB_NOTFOUND(1010, "This lab does not exist!!"),
    LAB_NAMEDUPLICATED(1011, "This lab name has already been taken!!"),
    LAB_UPLOAD_FAILED(1012, "Upload failed"),
    LAB_UNSUPPORTED_FILENAME(1013, "Unsupported filename!!"),
    LAB_DOWNLOAD_FAILED(1014, "Download failed"),
    LAB_FILENAME_DUPLICATED(1015, "This file PDF already exists!!"),
    LAB_END_NOT_PDF(1015, "Unsupported File Format!!"),

    // Product Errors
    PRODUCT_NAMEDUPLICATED(1016, "This product name has already been taken!!"),
    PRODUCT_NOTFOUND(1017, "This product does not exist!!"),
    PRODUCT_UNAVAILABLE(1018, "This product is out of stock"),

    // File Errors
    UPLOAD_FILE_FAILED(1019, "Failed to upload this file!!"),

    // Tag Errors
    TAG_NAME_DUPLICATED(1020, "This tag name has already been taken"),
    TAG_NOTFOUND(1021, "Tag not found"),

    // Category Errors
    CATEGORY_NOTFOUND(1022, "Category not found"),
    CATEGORY_NAME_DUPLICATED(1023, "This category name has already been taken"),

    // Blog Errors
    BLOG_DUPLICATED(1024, "This blog name has already been taken"),
    BLOG_NOTFOUND(1025, "Blog not found"),

    // Token Errors
    INVALID_TOKEN(1026, "Invalid token"),

    // SQL Execution Errors
    EXECUTED_FAILED(1027, "Execution failed"),

    // Cart Errors
    CART_NOTFOUND(1028, "Cart not found"),

    // Relationship Errors
    CATE_PRODUCT_NOTFOUND(1029, "This relationship not found"),

    // Question Errors
    QUESTION_NOTFOUND(1030, "Question not found"),

    // Feedback Errors
    FEEDBACK_NOTFOUND(1031, "Feedback not found"),

    // Wishlist Errors
    WISHLIST_NOTFOUND(1032, "Wishlist not found"),
    WISHLIST_EXIST(1033, "This product has already been added to Wishlist"),

    // Order Status Errors
    ORDER_STATUS_NOTFOUND(1034, "Order status not found"),
    ORDER_STATUS_EXIST(1035, "Order status already exists"),

    // Order Errors
    ORDERS_NOTFOUND(1036, "Orders not found"),
    ORDER_CREATION_FAILED(1037, "Order creation failed"),
    ORDERDETAILS_NOTFOUND(1038, "Order details not found"),
    ORDER_FAILED(1039, "Order failed"),
    ORDER_CANCEL_FAILED(1039, "Cancel Order failed. You only able to cancel your Order if your Order is pending"),
    INVALID_UPDATE_STATUS_DOWNDATE(1040, "Cannot downdate Order's status"),
    INVALID_UPDATE_STATUS_UPDATE(1040, "You must update the previous Order's status first"),

    // Support Errors
    SUPPORTING_NOT_FOUND(1040, "Support not found"),
    SUPPORTING_LIMITED(1041, "Support out of limit!!"),
    SUPPORTING_UNSUPPORTED_STATUS_CODE(1042, "Unsupported status code!!"),
    SUPPORTING_INVALID_SUPPORT_DATE(1043, "Invalid support date!!"),
    SUPPORTING_LAB_EXISTING(1044, "Your previous lab supporting request has not been completed yet!!"),
    SUPPORTING_LAB_DONE(1045, "Your lab supporting request has been completed!!"),
    SUPPORTING_DATE_NULL(1046, "Support date must not be null!!"),
    SUPPORTING_INVALID_STATUS(1047, "Invalid order status (received -> approved -> done)!!"),
    // Payment Errors
    PAYMENT_FAIL(1048, "Payment failed!!"),
    PAYMENT_INVALID_SIGN(1049, "Invalid payment sign!!"),

    // Lab Guide
    LAB_GUIDE_NOT_FOUND(1050, "Lab guide not found !!"),

    // Image
    IMAGE_NOT_FOUND(1051, "Image not found!!"),
    // Own
    OWN_NOT_EXIST(1052, "Account has not this labID !!!"),
    OWN_IS_EXIST(1053, "This LabID and AccountID existed !!!"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
