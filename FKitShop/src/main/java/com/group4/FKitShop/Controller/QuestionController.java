package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Question;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.QuestionRequest;
import com.group4.FKitShop.Service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {

    QuestionService questionService;

    // get all question
    @GetMapping()
    public List<Question> allQuestions() {
        return questionService.allQuestions();
    }


    // get question by ID
    @GetMapping("/{questionID}")
    ResponseEntity<ResponseObject> getQuestionByID(@PathVariable int questionID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", questionService.getQuestionByID(questionID))
        );
    }

    // get list question by labID
    @GetMapping("/byLabID/{labID}")
    ResponseEntity<ResponseObject> getQuestionByLabID(@PathVariable String labID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", questionService.getQuestionByLabID(labID))
        );
    }

    // get list question by accountID
    @GetMapping("/byAccountID/{accountID}")
    ResponseEntity<ResponseObject> getQuestionByAccountID(@PathVariable String accountID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", questionService.getQuestionByAccountID(accountID))
        );
    }

    // create question
    @PostMapping()
    public ResponseObject createQuestion(@RequestBody QuestionRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create question successfully")
                .data(questionService.createQuestion(request))
                .build();
    }

    @PutMapping("/{questionID}")
    public ResponseObject updateQuestion(@RequestBody QuestionRequest request, @PathVariable int questionID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update question successfully")
                .data(questionService.updateQuestion(questionID, request))
                .build();
    }

    // delete feedback
    @DeleteMapping("/{questionID}")
    public ResponseObject deleteQuestion(@PathVariable int questionID) {
        questionService.deleteQuestion(questionID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete question successfully")
                .build();
    }
}
