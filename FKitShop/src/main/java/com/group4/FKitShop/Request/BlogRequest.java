package com.group4.FKitShop.Request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequest {

    @Size(min = 1, max = 100, message = "Blog name must not more than 100 characters")
    private String blogName;
    private String content;
    private String status;
    private String accountID;
    private int toDelete;


    // in order to crud BlogTag table
    private List<Integer> tagID;

}


