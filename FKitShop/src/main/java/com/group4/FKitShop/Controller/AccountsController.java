package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.UpdateInfoCustomerRequest;
import com.group4.FKitShop.Request.UpdatePassword;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Service.AccountsService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountsController {

    AccountsService accountsService;

    @PostMapping("/register")
    public ResponseObject signUp(@RequestBody @Valid AccountsRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create account successfully")
                .data(accountsService.signUp(request))
                .build();
    }

    @GetMapping("/allAccounts")
    public ResponseObject allAccounts() {
        return ResponseObject.builder()
                .status(1000)
                .message("Get All Accounts Successfully !!")
                .data(accountsService.allAccounts())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject getAccountByID(@PathVariable String id) {
        Optional<Accounts> accounts = accountsService.getAccountByID(id);
        return ResponseObject.builder()
                .data(accounts)
                .message("Found Account Successfully")
                .build();
    }

    @PutMapping("/customer/updateinfo/{id}")
    ResponseEntity<ResponseObject> updateInfo(@PathVariable String id, @RequestBody UpdateInfoCustomerRequest request) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Update Information Successfully !!")
                        .data(accountsService.updateInfo(id, request))
                        .build()
        );
    }

    @PostMapping("/avatar/{id}")
    ResponseEntity<ResponseObject> uploadAvatar(@PathVariable String id, @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Upload Avatar Successfully !!")
                        .data(accountsService.uploadAvatar(id, image))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteAccount(@PathVariable String id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Delete Account Successfully !!")
                        .data(accountsService.deleteAccount(id))
                        .build()
        );
    }

    @GetMapping("/listAccounts")
    ResponseEntity<ResponseObject> getActiveAccounts() {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Get All Actice Account Successfully !!")
                        .data(accountsService.getActiveAccounts())
                        .build()
        );
    }

    @PutMapping("/password/{id}")
    ResponseEntity<ResponseObject> updatePassword(@RequestBody UpdatePassword request, @PathVariable String id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(1000)
                        .message("Update Password Successfully !!")
                        .data(accountsService.updatePassword(request, id))
                        .build()
        );
    }

    @PostMapping("/createAccount")
    public ResponseObject createAccount(@RequestBody @Valid AccountsRequest request) {
        return ResponseObject.builder()
                .status(1000)
                .message("Create account successfully")
                .data(accountsService.createAccount(request))
                .build();
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<ResponseObject> updateAccount(@RequestBody AccountsRequest request, @PathVariable String id) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update infomation successfully")
                        .data(accountsService.updateAccount(request, id))
                        .build()
        );
    }

}
