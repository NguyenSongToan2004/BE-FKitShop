package com.group4.FKitShop.Request;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCategoryRequest {

    private String categoryID;
    private int tagID;
    private String categoryName;
    private String description;

}