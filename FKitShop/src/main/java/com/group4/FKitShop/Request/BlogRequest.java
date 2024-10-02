package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequest {

    @Size(min = 5, max = 100, message = "Blog name must not more than 100 characters")
    private String blogName;
    private String content;
    private String status;
    //private String image;
    private String accountID;

}


