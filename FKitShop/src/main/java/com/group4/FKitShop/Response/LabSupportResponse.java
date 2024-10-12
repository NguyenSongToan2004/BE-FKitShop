package com.group4.FKitShop.Response;

import com.group4.FKitShop.Response.SubResponse.LabSupport;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabSupportResponse {
    String accountID;
    String customerName;
    List<LabSupport> labSupports;
}
