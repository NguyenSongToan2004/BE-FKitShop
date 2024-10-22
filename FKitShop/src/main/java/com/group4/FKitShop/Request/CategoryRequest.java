package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CategoryRequest {

    private int tagID;
    @Size(min = 1, max = 100, message = "Category name must not more than 100 characters")
    private String categoryName;
    private String description;
    private int status;

    // crud CateProduct table
    //private List<String> productID;

}
