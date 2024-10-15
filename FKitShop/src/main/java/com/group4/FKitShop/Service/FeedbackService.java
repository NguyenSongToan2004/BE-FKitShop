package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.FeedbackMapper;
import com.group4.FKitShop.Repository.FeedbackRepository;
import com.group4.FKitShop.Request.FeedbackRequest;
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
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;

    public List<Feedback> allFeedbacks(){
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackByID(int id){
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOTFOUND));
        return feedback;
    }

    public List<Feedback> getFeedbackByAccountID(String id){
        return feedbackRepository.getFeedbackByAccountID(id);
    }

    public List<Feedback> getFeedbackByProductID(String id){
        return feedbackRepository.getFeedbackByProductID(id);
    }

    public Feedback createFeedback(FeedbackRequest request) {
        Feedback feedback = feedbackMapper.toFeedback(request);
        feedback.setCreateDate(new Date());
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(int id, FeedbackRequest request){
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.FEEDBACK_NOTFOUND));
        Feedback fb = feedbackMapper.toFeedback(request);
        fb.setAccountID(feedback.getAccountID());
        fb.setProductID(feedback.getProductID());
        fb.setFeedbackID(id);
        fb.setCreateDate(new Date());
        return feedbackRepository.save(fb);
    }

    @Transactional
    public void deleteFeedback(int id) {
        if (!feedbackRepository.existsById(id))
            throw new AppException(ErrorCode.FEEDBACK_NOTFOUND);
        feedbackRepository.deleteById(id);
    }

   }
