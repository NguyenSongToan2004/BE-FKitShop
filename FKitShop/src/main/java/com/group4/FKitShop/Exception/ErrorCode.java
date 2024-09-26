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

    TagName_DUPLICATED(1001, "This tag name has been taken"),
    CategoryName_DUPLICATED(1001, "This category name has been taken"),

    Tag_NOTFOUND(1002, "Tag not found"),
    Category_NOTFOUND(1002, "Category not found"),




    ;

    int code;
    String message;

}
