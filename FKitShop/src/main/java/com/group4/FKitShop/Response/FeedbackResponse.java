package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
    Feedback feedback;
    String customerName;
    Product product;
}
