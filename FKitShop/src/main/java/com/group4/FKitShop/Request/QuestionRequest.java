package com.group4.FKitShop.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionRequest {

    private String labID;
    private String accountID;
    private String description;
    private int status;
    private String response;
}
