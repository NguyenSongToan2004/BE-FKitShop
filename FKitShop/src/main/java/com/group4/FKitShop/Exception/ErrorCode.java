package com.group4.FKitShop.Exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    Uncategorize_Exception(9999, "Uncategorized exception"),
    UPLOAD_FILE_FAILED(1010, "Fail to upload this file!!"),

    TagName_DUPLICATED(1001, "This tag name has been taken"),
    CategoryName_DUPLICATED(1001, "This category name has been taken"),
    Blog_DUPLICATED(1001, "This blog name has been taken"),

    Tag_NOTFOUND(1002, "Tag not found"),
    Category_NOTFOUND(1002, "Category not found"),
    Blog_NOTFOUND(1002, "Blog not found"),






    ;

    int code;
    String message;

}
