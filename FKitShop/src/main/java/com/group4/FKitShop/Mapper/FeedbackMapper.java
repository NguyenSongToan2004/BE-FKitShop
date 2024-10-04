package com.group4.FKitShop.Mapper;


import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Request.FeedbackRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    Feedback toFeedback(FeedbackRequest request);
}
