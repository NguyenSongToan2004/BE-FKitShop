package com.group4.FKitShop.Mapper;

import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Request.QuestionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question toQuestion(QuestionRequest request);
}
