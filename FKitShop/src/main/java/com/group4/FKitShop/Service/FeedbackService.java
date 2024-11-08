package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.FeedbackMapper;
import com.group4.FKitShop.Repository.FeedbackRepository;
import com.group4.FKitShop.Repository.ProductRepository;
import com.group4.FKitShop.Request.FeedbackRequest;
import com.group4.FKitShop.Response.FeedbackResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService {

    FeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;
    AccountsService accountsService;
    ProductRepository productRepository;

    public List<FeedbackResponse> allFeedbacks(){
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            String customerName = (accountsService.getAccountByID(feedback.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(feedback.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            feedbackResponse.setFeedback(feedback);
            feedbackResponse.setCustomerName(customerName);
            feedbackResponse.setProduct(product);
            feedbackResponses.add(feedbackResponse);
        }
        return feedbackResponses;
    }

    public FeedbackResponse getFeedbackByID(int id){
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOTFOUND));
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        String customerName = (accountsService.getAccountByID(feedback.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        Product product = productRepository.findById(feedback.getProductID()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        feedbackResponse.setFeedback(feedback);
        feedbackResponse.setCustomerName(customerName);
        feedbackResponse.setProduct(product);
        return feedbackResponse;
    }

    public List<FeedbackResponse> getFeedbackByAccountID(String id){
        List<Feedback> feedbacks = feedbackRepository.findByAccountID(id);
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            String customerName = (accountsService.getAccountByID(feedback.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(feedback.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            feedbackResponse.setFeedback(feedback);
            feedbackResponse.setCustomerName(customerName);
            feedbackResponse.setProduct(product);
            feedbackResponses.add(feedbackResponse);
        }
        return feedbackResponses;
    }

    public List<FeedbackResponse> getFeedbackByProductID(String id){
        List<Feedback> feedbacks = feedbackRepository.findByProductID(id);
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            String customerName = (accountsService.getAccountByID(feedback.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            Product product = productRepository.findById(feedback.getProductID()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            feedbackResponse.setFeedback(feedback);
            feedbackResponse.setCustomerName(customerName);
            feedbackResponse.setProduct(product);
            feedbackResponses.add(feedbackResponse);
        }
        return feedbackResponses;
    }


    public Feedback createFeedback(FeedbackRequest request) {
        Feedback feedback = feedbackMapper.toFeedback(request);
        feedback.setCreateDate(new Date(System.currentTimeMillis()));
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(int id, FeedbackRequest request){
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.FEEDBACK_NOTFOUND));
        Feedback fb = feedbackMapper.toFeedback(request);
        fb.setAccountID(feedback.getAccountID());
        fb.setProductID(feedback.getProductID());
        fb.setFeedbackID(id);
        fb.setCreateDate(new Date(System.currentTimeMillis()));
        return feedbackRepository.save(fb);
    }

    @Transactional
    public void deleteFeedback(int id) {
        if (!feedbackRepository.existsById(id))
            throw new AppException(ErrorCode.FEEDBACK_NOTFOUND);
        feedbackRepository.deleteById(id);
    }
}
