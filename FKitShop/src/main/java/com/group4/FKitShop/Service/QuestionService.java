package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Entity.Tag;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.FeedbackMapper;
import com.group4.FKitShop.Mapper.QuestionMapper;
import com.group4.FKitShop.Repository.QuestionRepository;
import com.group4.FKitShop.Repository.TagRepository;
import com.group4.FKitShop.Request.FeedbackRequest;
import com.group4.FKitShop.Request.QuestionRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {

    QuestionRepository questionRepository;
    QuestionMapper questionMapper;

    public List<Question> allQuestions(){
        return questionRepository.findAll();
    }

    public Question getQuestionByID(int id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Question_NOTFOUND));
        return question;
    }

    public Question createQuestion(QuestionRequest request) {
        Question question = questionMapper.toQuestion(request);
        question.setStatus(0);
        return questionRepository.save(question);
    }

    public Question updateQuestion(int id, QuestionRequest request){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.Question_NOTFOUND));

        question.setResponse(request.getResponse());
        question.setResponseDate(new Date());
        question.setStatus(1);
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(int id) {
        if (!questionRepository.existsById(id))
            throw new AppException(ErrorCode.Feedback_NOTFOUND);
        questionRepository.deleteById(id);
    }
}
