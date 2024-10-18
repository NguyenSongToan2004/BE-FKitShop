package com.group4.FKitShop.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {

    int questionID;
    String labID;
    //1
    String labName;
    String accountID;
    //2
    String customerName;
    String description;
    int status;
    String response;
    Date postDate;
    Date responseDate;


}
