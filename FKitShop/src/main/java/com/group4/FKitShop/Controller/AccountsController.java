package com.group4.FKitShop.Controller;

import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AddCustomerRequest;
import com.group4.FKitShop.Request.UpdateInfoCustomerRequest;
import com.group4.FKitShop.Request.UpdatePassword;
import com.group4.FKitShop.Service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
public class AccountsController {

    @Autowired
    private AccountsService service;

    @PostMapping("/customer/add")
    ResponseEntity<ResponseObject> createCustomer(@RequestBody AddCustomerRequest request){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Create Account Successfully !!")
                        .data(service.addCustomer(request))
                        .build()
        );
    }

    @GetMapping("/customer/{id}")
    ResponseEntity<ResponseObject> getCustomerByID(@PathVariable String id){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Found Customer Successfully !!")
                        .data(service.getCustomerByID(id))
                        .build()
        );
    }

    @PutMapping("/customer/updateinfo/{id}")
    ResponseEntity<ResponseObject> updateInfo(@PathVariable String id, @RequestBody UpdateInfoCustomerRequest request){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Update Information Successfully !!")
                        .data(service.updateInfo(id, request))
                        .build()
        );
    }

    @PostMapping("/avatar/{id}")
    ResponseEntity<ResponseObject> uploadAvatar(@PathVariable String id, @RequestParam("image")MultipartFile image){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Upload Avatar Successfully !!")
                        .data(service.uploadAvatar(id, image))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteAccount(@PathVariable String id){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Delete Account Successfully !!")
                        .data(service.deleteAccount(id))
                        .build()
        );
    }

    @GetMapping("/listAccounts")
    ResponseEntity<ResponseObject> getActiveAccounts(){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Get All Actice Account Successfully !!")
                        .data(service.getActiveAccounts())
                        .build()
        );
    }

    @GetMapping("/allAccounts")
    ResponseEntity<ResponseObject> getAllAccounts(){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Get All Accounts Successfully !!")
                        .data(service.getAllAccounts())
                        .build()
        );
    }

    @PutMapping("/password/{id}")
    ResponseEntity<ResponseObject> updatePassword(@RequestBody UpdatePassword request, @PathVariable String id){
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Update Password Successfully !!")
                        .data(service.updatePassword(request, id))
                        .build()
        );
    }

}
