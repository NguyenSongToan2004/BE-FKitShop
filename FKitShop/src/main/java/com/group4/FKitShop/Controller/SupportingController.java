package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Entity.Supporting;
import com.group4.FKitShop.Request.SupportStatusUpdateRequest;
import com.group4.FKitShop.Request.SupportingRequest;
import com.group4.FKitShop.Service.SupportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<ResponseObject> updateStatus(@RequestBody SupportStatusUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(1000, "Update status successfully !!", service.updateStatus(request))
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
}
