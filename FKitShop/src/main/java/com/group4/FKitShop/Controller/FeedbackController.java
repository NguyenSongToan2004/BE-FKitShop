package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.Feedback;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.FeedbackRequest;
import com.group4.FKitShop.Response.FeedbackResponse;
import com.group4.FKitShop.Service.FeedbackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackController {

    FeedbackService feedbackService;

    // get all feedback
    @GetMapping()
    public List<FeedbackResponse> allFeedbacks() {
        return feedbackService.allFeedbacks();
    }


    // get feedback by ID
    @GetMapping("/{feedbackID}")
    ResponseEntity<ResponseObject> getFeedbackByID(@PathVariable int feedbackID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", feedbackService.getFeedbackByID(feedbackID))
        );
    }

    // get list feedback by productID
    @GetMapping("/byProductID/{productID}")
    ResponseEntity<ResponseObject> getFeedbackByProductID(@PathVariable String productID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", feedbackService.getFeedbackByProductID(productID))
        );
    }

    // get list feedback by accountID
    @GetMapping("/byAccountID/{accountID}")
    ResponseEntity<ResponseObject> getFeedbackByAccountID(@PathVariable String accountID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Found successfully", feedbackService.getFeedbackByAccountID(accountID))
        );
    }

    // create feedback
    @PostMapping()
    public ResponseObject createFeedback(@RequestBody FeedbackRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create feedback successfully")
                .data(feedbackService.createFeedback(request))
                .build();
    }

    @PutMapping("/{feedbackID}")
    public ResponseObject updateFeedback(@RequestBody FeedbackRequest request, @PathVariable int feedbackID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Update feedback successfully")
                .data(feedbackService.updateFeedback(feedbackID, request))
                .build();
    }

    // delete feedback
    @DeleteMapping("/{feedbackID}")
    public ResponseObject deleteFeedback(@PathVariable int feedbackID) {
        feedbackService.deleteFeedback(feedbackID);
        return ResponseObject.builder()
                .status(1000)
                .message("Delete feedback successfully")
                .build();
    }
}
