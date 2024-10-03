package com.group4.FKitShop.Controller;


import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Entity.ResponseObject;
import com.group4.FKitShop.Request.AccountsRequest;
import com.group4.FKitShop.Request.TokenRequest;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Service.AccountsService;
import com.group4.FKitShop.Service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountsController {
    AccountsService accountsService;
    AuthenticationService authenticationService;

    @GetMapping
    public ResponseObject allAccounts(){
        return ResponseObject.builder()
                .data(accountsService.allAccounts())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject AccountByID(@PathVariable String id){
        Optional<Accounts> accounts = accountsService.AccountsByID(id);
        return ResponseObject.builder()
                .data(accounts)
                .build();
    }

    @GetMapping("/info")
    public ResponseObject AccountsInfo(@RequestBody TokenRequest token){
        String tokenrequest = token.getToken();
        AccountsResponse accountsResponse = authenticationService.tokenAccountResponse(tokenrequest);
        return ResponseObject.builder()
                .data(accountsResponse)
                .build();
    }

}
