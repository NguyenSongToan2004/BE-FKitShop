package com.group4.FKitShop.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // instead of @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)  // set all field to private
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject {
    int code;
    String message;
    Object o;
}
