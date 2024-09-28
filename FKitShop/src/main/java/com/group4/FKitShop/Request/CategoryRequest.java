package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CategoryRequest {

    private int tagID;
    @Size(min = 5, max = 100, message = "Category name must not more than 100 characters")
    private String categoryName;
    private String description;

}
