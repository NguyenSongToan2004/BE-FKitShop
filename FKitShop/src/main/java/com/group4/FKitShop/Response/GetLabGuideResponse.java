package com.group4.FKitShop.Response;

import com.group4.FKitShop.Entity.LabGuide;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetLabGuideResponse {
    String labID;
    List<LabGuide> labGuides;
}
