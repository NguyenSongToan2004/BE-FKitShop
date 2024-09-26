package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Service.AccountsService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountsController {
    AccountsService accountsService;

    @PostMapping
    public ResponseObject createAccounts(@RequestBody @Valid AccountsRequest request){
        return ResponseObject.builder()
                .status(1000)
                .message("Create account successfully")
                .data(accountsService.createAccount(request))
                .build();
    }

    @GetMapping
    public ResponseObject allAccounts(){
        return ResponseObject.builder()
                .data(accountsService.allAccounts())
                .build();
    }

}
