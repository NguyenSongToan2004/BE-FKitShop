package com.group4.FKitShop.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackRequest {

    private String accountID;
    private String productID;
    private String description;
    private String rate;
}
