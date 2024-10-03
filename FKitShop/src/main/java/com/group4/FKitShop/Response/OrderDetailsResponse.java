package com.group4.FKitShop.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsResponse {

    String orderDetailsID;
    String productID;
    String ordersID;
    int quantity;
    double price;
    int isActive;
    int status;
    Date confirmDate;
    Date warrantyDate;

}
