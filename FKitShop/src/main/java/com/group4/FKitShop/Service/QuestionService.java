package com.group4.FKitShop.Service;


import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Exception.AppException;
import com.group4.FKitShop.Exception.ErrorCode;
import com.group4.FKitShop.Mapper.QuestionMapper;
import com.group4.FKitShop.Repository.QuestionRepository;
import com.group4.FKitShop.Request.QuestionRequest;
import com.group4.FKitShop.Response.QuestionResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {

    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    AccountsService accountsService;
    LabService labService;

    public List<QuestionResponse> allQuestions(){
        List<QuestionResponse> responses = new ArrayList<>();
        List<Question> questions = questionRepository.findAll();
        for (Question question : questions) {
            QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
            String customerName = (accountsService.getAccountByID(question.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            String labName = labService.getLab(question.getLabID()).getName();
            questionResponse.setCustomerName(customerName);
            questionResponse.setLabName(labName);
            responses.add(questionResponse);
        }

        return responses;
    }

    public QuestionResponse getQuestionByID(int id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOTFOUND));
        QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
        String customerName = (accountsService.getAccountByID(question.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        String labName = labService.getLab(question.getLabID()).getName();
        questionResponse.setCustomerName(customerName);
        questionResponse.setLabName(labName);
        return questionResponse;

    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        QuestionResponse questionResponse = new QuestionResponse();
        Question question = questionMapper.toQuestion(request);
        question.setStatus(0);
        question.setPostDate(new Date());
        questionRepository.save(question);
        questionResponse = questionMapper.toQuestionResponse(question);
        String customerName = (accountsService.getAccountByID(question.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        String labName = labService.getLab(question.getLabID()).getName();
        questionResponse.setCustomerName(customerName);
        questionResponse.setLabName(labName);
        return questionResponse;
    }

    public QuestionResponse updateQuestion(int id, QuestionRequest request){
        QuestionResponse questionResponse = new QuestionResponse();
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOTFOUND));
        Question ques = questionMapper.toQuestion(request);
        ques.setAccountID(question.getAccountID());
        ques.setPostDate(question.getPostDate());
        ques.setQuestionID(id);
        if(!request.getResponse().isEmpty()){
            ques.setResponseDate(new Date());
            ques.setStatus(1);
        }else{
            ques.setStatus(0);
            ques.setResponseDate(null);
        }
        questionRepository.save(ques);
        questionResponse = questionMapper.toQuestionResponse(question);
        String customerName = (accountsService.getAccountByID(question.getAccountID())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
        String labName = labService.getLab(question.getLabID()).getName();
        questionResponse.setCustomerName(customerName);
        questionResponse.setLabName(labName);
        return questionResponse;
    }

    @Transactional
    public void deleteQuestion(int id) {
        if (!questionRepository.existsById(id))
            throw new AppException(ErrorCode.FEEDBACK_NOTFOUND);
        questionRepository.deleteById(id);
    }

    public List<QuestionResponse> getQuestionByAccountID(String id){
        List<QuestionResponse> responses = new ArrayList<>();
        List<Question> questions = questionRepository.getQuestionByAccountID(id);
        for (Question question : questions) {
            QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
            String customerName = (accountsService.getAccountByID(question.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            String labName = labService.getLab(question.getLabID()).getName();
            questionResponse.setCustomerName(customerName);
            questionResponse.setLabName(labName);
            responses.add(questionResponse);
        }
        return responses;
    }

    public List<QuestionResponse> getQuestionByLabID(String id){
        List<QuestionResponse> responses = new ArrayList<>();
        List<Question> questions = questionRepository.getQuestionByLabID(id);
        for (Question question : questions) {
            QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
            String customerName = (accountsService.getAccountByID(question.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            String labName = labService.getLab(question.getLabID()).getName();
            questionResponse.setCustomerName(customerName);
            questionResponse.setLabName(labName);
            responses.add(questionResponse);
        }
        return responses;
    }

    public List<QuestionResponse> getQuestionByAccountAndLab(String account, String lab){
        List<QuestionResponse> responses = new ArrayList<>();
        List<Question> questions = questionRepository.getQuesByAccountAndLab(account, lab);
        for (Question question : questions) {
            QuestionResponse questionResponse = questionMapper.toQuestionResponse(question);
            String customerName = (accountsService.getAccountByID(question.getAccountID())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)).getFullName());
            String labName = labService.getLab(question.getLabID()).getName();
            questionResponse.setCustomerName(customerName);
            questionResponse.setLabName(labName);
            responses.add(questionResponse);
        }
        return responses;
    }

}


