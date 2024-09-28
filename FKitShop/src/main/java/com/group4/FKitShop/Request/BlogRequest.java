package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    private Integer tagID;
    @Size(min = 5, max = 100, message = "Blog name must not more than 100 characters")
    private String blogName;
    private String content;
    private String status;
    private String image;
    private String accountID;

}


