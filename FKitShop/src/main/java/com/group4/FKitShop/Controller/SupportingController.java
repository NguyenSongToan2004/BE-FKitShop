package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.SupportStatusUpdateRequest;
import com.group4.FKitShop.Request.SupportingRequest;
import com.group4.FKitShop.Request.UpdateExpectedDateRequest;
import com.group4.FKitShop.Request.UpdateSupportDate;
import com.group4.FKitShop.Service.SupportingService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/support")
public class SupportingController {

    @Autowired
    SupportingService service;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createSupport(@RequestBody SupportingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(1000, "Send request to support successfully !!", service.createSupporting(request))
        );
    }

    @PutMapping("/status")
    ResponseEntity<ResponseObject> updateStatus(@RequestBody SupportStatusUpdateRequest request) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update status successfully !!", service.updateStatus(request))
        );
    }

    @PutMapping("/support-date")
    ResponseEntity<ResponseObject> updateSupportDate(@RequestBody UpdateSupportDate request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update status successfully !!", service.updateSupportDate(request))
        );
    }

    @GetMapping("/all-supports")
    ResponseEntity<ResponseObject> getAllSupports() {
       return ResponseEntity.ok(
                new ResponseObject(1000, "Get all support successfully !!", service.getAllSupport())
        );
    }

    @GetMapping("/support-accountID/{accountID}")
    ResponseEntity<ResponseObject> getSupportByAccount(@PathVariable String accountID) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get list support sucessfully !! ", service.getSupportByAccount(accountID))
        );
    }

    @GetMapping("/support-status/{status}")
    ResponseEntity<ResponseObject> getSupportByAccount(@PathVariable int status) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get list support by status sucessfully !! ", service.getSupportByStatus(status))
        );
    }

    @GetMapping("/supports/{accountID}/{status}")
    ResponseEntity<ResponseObject> getSupportByAccount(@PathVariable String accountID, @PathVariable int status) {
        return ResponseEntity.ok(
                new ResponseObject(1000, "Get list support by account and status sucessfully !! ", service.getSupportByAccount(accountID, status))
        );
    }

    @PutMapping("/update-expected")
    ResponseEntity<ResponseObject> updateExpected(@RequestBody UpdateExpectedDateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update expected date successfully !!", service.updateExpectedDate(request))
        );
    }
}
