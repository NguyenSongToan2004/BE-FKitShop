package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TagRequest {

    @Size(min = 5, max = 100, message = "Tag name must not more than 100 characters")
    private String tagName;
    private String description;

}
